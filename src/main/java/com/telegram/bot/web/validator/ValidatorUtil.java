package com.telegram.bot.web.validator;

import com.telegram.bot.exception.BeanValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ValidatorUtil {

    private static final int SMALL_INT_MAX_NUMBER = 32_767;
    private static final int MAX_PAGE_SIZE = 50;
    private static final List<String> SORT_TYPES = Arrays.asList("asc", "desc");

    private ValidatorUtil() {}

    public static void validateNonNullWithMessage(Object obj, String message) {
        if (Objects.isNull(obj)) {
            throw new BeanValidationException(message);
        }
    }

    public static void validateId(Integer id) {
        validateNonNullWithMessage(id, "Id must not be null");
        if (id <= 0) {
            throw new BeanValidationException("Id must be positive");
        }
        if (id >= SMALL_INT_MAX_NUMBER) {
            throw new BeanValidationException("Id is to big");
        }
    }

    public static void validatePagination(Integer page, Integer size) {
        validateNonNullWithMessage(page, "Page must not be null");
        validateNonNullWithMessage(size, "Size must not be null");

        if (page <= 0) {
            throw new BeanValidationException("Page must be positive");
        }
        if (size <= 0) {
            throw new BeanValidationException("Size must be positive");
        }
        if (size >= MAX_PAGE_SIZE) {
            throw new BeanValidationException("Size size is to big");
        }
    }

    public static void validateSortWithFields(String sortType, String sortBy, List<String>fields) {
        if (SORT_TYPES.stream().noneMatch(sortType::equalsIgnoreCase))
        {
            throw new BeanValidationException("Illegal sort type");
        }

        if (fields.stream().noneMatch(sortBy::equalsIgnoreCase))
        {
            throw new BeanValidationException("Illegal sort by");
        }
    }
}
