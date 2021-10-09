package com.telegram.bot.service;

import com.telegram.bot.exception.BeanNotFoundException;
import com.telegram.bot.model.Country;

import java.util.List;

public interface CountryService {

    Country getCountryById(Integer id) throws BeanNotFoundException;
    List<Country> getAllCountries(Integer page, Integer size, String sortType, String sortBy)
            throws BeanNotFoundException;
}
