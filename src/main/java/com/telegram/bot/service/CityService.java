package com.telegram.bot.service;

import com.telegram.bot.exception.BeanCreationException;
import com.telegram.bot.exception.BeanModificationException;
import com.telegram.bot.exception.BeanNotFoundException;
import com.telegram.bot.model.City;

import java.util.List;

public interface CityService {

    City getCityById(Integer id) throws BeanNotFoundException;
    City getByName(String name);
    List<City> getAllCities(Integer page, Integer size, String sortType, String sortBy)
            throws BeanNotFoundException;
    City addCity(City city) throws BeanCreationException;
    City updateCity(City city, Integer id) throws BeanModificationException;
    void deleteCity(Integer id) throws BeanModificationException;
}
