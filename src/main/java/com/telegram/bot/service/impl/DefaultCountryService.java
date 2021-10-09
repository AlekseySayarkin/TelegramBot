package com.telegram.bot.service.impl;

import com.telegram.bot.exception.BeanNotFoundException;
import com.telegram.bot.model.Country;
import com.telegram.bot.repository.CountryRepository;
import com.telegram.bot.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.telegram.bot.service.util.SortUtil.getPageRequest;

@Service
@Slf4j
public class DefaultCountryService implements CountryService {

    private static final String FAILED_TO_FIND_COUNTRY_ERROR_MESSAGE = "Failed to find country by id";
    private static final String FAILED_TO_FIND_COUNTRIES_ERROR_MESSAGE = "Failed to find all counties";

    private final CountryRepository countryRepository;

    public DefaultCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country getCountryById(Integer id) throws BeanNotFoundException {
        return countryRepository.findById(id).orElseThrow(() -> {
            log.error(FAILED_TO_FIND_COUNTRY_ERROR_MESSAGE + "{}", id);
            return new BeanNotFoundException(FAILED_TO_FIND_COUNTRY_ERROR_MESSAGE);
        });
    }

    @Override
    public List<Country> getAllCountries(Integer page, Integer size, String sortType, String sortBy)
            throws BeanNotFoundException {
        try {
            log.info("Searching countries by page: {}, size: {}, sortType: {}, sortBy: {}", page, size, sortType, sortBy);
            return countryRepository.findAll(getPageRequest(page, size, sortType, sortBy)).getContent();
        } catch (DataAccessException e) {
            log.error(FAILED_TO_FIND_COUNTRIES_ERROR_MESSAGE);
            throw new BeanNotFoundException(FAILED_TO_FIND_COUNTRIES_ERROR_MESSAGE);
        }
    }
}
