package com.telegram.bot.web.validator;

import com.telegram.bot.web.dto.CityDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static com.telegram.bot.web.validator.ValidatorUtil.validateNonNullWithMessage;

public class CityValidator extends AbstractCityValidator {

    @Configuration
    public static class MatchValidatorConfig{
        @Bean
        public Validator cityValidator() {
            return new CityValidator(true);
        }

        @Bean
        public Validator nullableCityValidator() {
            return new CityValidator(false);
        }
    }

    private final boolean notNullable;

    public CityValidator(boolean notNullable) {
        this.notNullable = notNullable;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Objects.equals(CityDTO.class, clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateNonNullWithMessage(target, "City must not be null");

        var city = (CityDTO) target;
        validateCity(notNullable, city, errors);
    }
}
