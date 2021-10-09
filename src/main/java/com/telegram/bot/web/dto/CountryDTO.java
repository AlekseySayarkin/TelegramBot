package com.telegram.bot.web.dto;

import com.telegram.bot.model.Country;
import com.telegram.bot.model.City;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CountryDTO {

    private Integer id;
    private String name;

    public static CountryDTO of(Country country) {
        return builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    public Country toCountry() {
        var country = new Country();
        country.setId(id);
        country.setName(name);
        return country;
    }
}
