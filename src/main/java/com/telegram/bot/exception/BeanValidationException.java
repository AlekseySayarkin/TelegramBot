package com.telegram.bot.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class BeanValidationException extends RuntimeException {

    private final transient Errors errors;

    public BeanValidationException(Errors errors) {
        this.errors = errors;
    }

    public BeanValidationException(String message) {
        super(message);
        errors = null;
    }
}
