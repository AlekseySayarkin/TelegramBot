package com.telegram.bot.service.impl;

import com.telegram.bot.exception.BeanCreationException;
import com.telegram.bot.exception.BeanModificationException;
import com.telegram.bot.exception.BeanNotFoundException;
import com.telegram.bot.model.City;
import com.telegram.bot.repository.CityRepository;
import com.telegram.bot.repository.CountryRepository;
import com.telegram.bot.service.CityService;
import com.telegram.bot.service.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DefaultCityService implements CityService {

    private static final String FAILED_TO_FIND_CITY_ERROR_MESSAGE = "Failed to find city by id";
    private static final String FAILED_TO_FIND_CITIES_ERROR_MESSAGE = "Failed to find all cities";
    private static final String FAILED_TO_SAVE_CITY_ERROR_MESSAGE = "Failed to save city";
    private static final String FAILED_TO_UPDATE_CITY_ERROR_MESSAGE = "Failed to update city";
    private static final String FAILED_TO_DELETE_CITY_ERROR_MESSAGE = "Failed to delete city";

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public DefaultCityService(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public City getCityById(Integer id) throws BeanNotFoundException {
        return cityRepository.findById(id).orElseThrow(() -> {
            log.error(FAILED_TO_FIND_CITY_ERROR_MESSAGE + " :{}", id);
            return new BeanNotFoundException(FAILED_TO_FIND_CITY_ERROR_MESSAGE);
        });
    }

    @Override
    public List<City> getAllCities(Integer page, Integer size, String sortType, String sortBy) throws BeanNotFoundException {
        try {
            log.info("Searching cities by page: {}, size: {}, sortType: {}, sortBy: {}", page, size, sortType, sortBy);
            return cityRepository.findAll(SortUtil.getPageRequest(page, size, sortType, sortBy)).getContent();
        } catch (DataAccessException e) {
            log.error(FAILED_TO_FIND_CITIES_ERROR_MESSAGE);
            throw new BeanNotFoundException(FAILED_TO_FIND_CITIES_ERROR_MESSAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = BeanCreationException.class)
    public City addCity(City city) throws BeanCreationException {
        try {
            city.setCountry(countryRepository.getById(city.getCountry().getId()));
            return cityRepository.save(city);
        } catch (DataAccessException e) {
            log.error(FAILED_TO_SAVE_CITY_ERROR_MESSAGE);
            throw new BeanCreationException(FAILED_TO_SAVE_CITY_ERROR_MESSAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = BeanModificationException.class)
    public City updateCity(City city, Integer id) throws BeanModificationException {
        try {
            var existingCity = getCityById(id);
            updateExistingCityFields(existingCity, city);

            return cityRepository.save(existingCity);
        } catch (DataAccessException e) {
            log.error(FAILED_TO_UPDATE_CITY_ERROR_MESSAGE);
            throw new BeanModificationException(FAILED_TO_UPDATE_CITY_ERROR_MESSAGE);
        }
    }

    private void updateExistingCityFields(City existingCity, City city) {
        existingCity.setName(Objects.nonNull(city.getName()) ? city.getName() : existingCity.getName());
        existingCity.setInfo(Objects.nonNull(city.getInfo()) ? city.getInfo() : existingCity.getInfo());
        existingCity.setCountry(Objects.nonNull(city.getCountry().getId()) ?
                countryRepository.getById(city.getCountry().getId()) : existingCity.getCountry());
    }

    @Override
    @Transactional(rollbackFor = BeanModificationException.class)
    public void deleteCity(Integer id) throws BeanModificationException {
        try {
            cityRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error(FAILED_TO_DELETE_CITY_ERROR_MESSAGE);
            throw new BeanModificationException(FAILED_TO_DELETE_CITY_ERROR_MESSAGE);
        }
    }
}
