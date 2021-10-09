package com.telegram.bot.web.controller;

import com.telegram.bot.exception.BeanCreationException;
import com.telegram.bot.exception.BeanModificationException;
import com.telegram.bot.exception.BeanNotFoundException;
import com.telegram.bot.exception.BeanValidationException;
import com.telegram.bot.web.dto.ExceptionResponseDTO;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class BaseController {

    protected static final String COUNTRY = "country";

    protected void validate(final Object object, final String objectName, final Validator validator)
    {
        final Errors errors = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, errors);
        if (errors.hasErrors())
        {
            throw new BeanValidationException(errors);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BeanValidationException.class)
    public ExceptionResponseDTO handleBeanValidationExceptionException(final BeanValidationException ex)
    {
        log.debug(ex.getClass().getName(), ex);

        var errors = new ArrayList<String>();
        if (Objects.nonNull(ex.getErrors()))
        {
            errors.addAll(ex.getErrors().getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
        }
        if (Objects.nonNull(ex.getMessage()))
        {
            errors.add(ex.getMessage());
        }

        return ExceptionResponseDTO
                .builder()
                .errors(errors)
                .build();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({ BeanNotFoundException.class, BeanCreationException.class, BeanModificationException.class })
    public ExceptionResponseDTO handleBeanNotFoundExceptionException(final RuntimeException ex)
    {
        log.debug(ex.getClass().getName(), ex);
        return ExceptionResponseDTO
                .builder()
                .errors(List.of(ex.getMessage()))
                .build();
    }
}
