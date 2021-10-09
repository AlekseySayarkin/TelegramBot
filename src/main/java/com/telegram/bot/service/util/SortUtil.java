package com.telegram.bot.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class SortUtil {

    private SortUtil() {}

    public static PageRequest getPageRequest(Integer page, Integer size, String sortType, String sortBy) {
        return PageRequest.of(--page, size, getSort(sortType, sortBy));
    }

    public static Sort getSort(String sortType, String sortBy) {
        return sortType.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }
}
