package com.telegram.bot.web.validator;

import com.telegram.bot.web.dto.CityDTO;
import com.telegram.bot.web.dto.CountryDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static com.telegram.bot.web.validator.ValidatorUtil.validateNonNullWithMessage;

public abstract class AbstractCityValidator implements Validator {

    protected void validateCity(boolean notNullable, CityDTO city, Errors errors) {
        if (notNullable) {
            validateNonNullWithMessage(city.getName(), "city name team must not be null");
            validateNonNullWithMessage(city.getInfo(), "city info team must not be null");
            validateNonNullWithMessage(city.getCountry(), "city country team must not be null");
        }
        if (Objects.nonNull(city.getCountry())) {
            validateCountry(city.getCountry());
        }
        if (Objects.nonNull(city.getName())) {
            validateString(city.getName(), errors);
        }
        if (Objects.nonNull(city.getInfo())) {
            validateString(city.getInfo(), errors);
        }
    }

    private void validateString(String string, Errors errors) {
        if (string.length() > 100) {
            errors.reject("string", "length is to big");
        }
        if (string.length() < 3) {
            errors.reject("string", "length is to small");
        }
    }

    private void validateCountry(CountryDTO country) {
        ValidatorUtil.validateId(country.getId());
    }
}
