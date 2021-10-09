package com.telegram.bot.web.dto;

import com.telegram.bot.model.City;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class CityDTO {

    private Integer id;
    private String name;
    private String info;
    private CountryDTO country;

    public static CityDTO of(City city) {
        return builder()
                .id(city.getId())
                .name(city.getName())
                .info(city.getInfo())
                .country(CountryDTO.of(city.getCountry()))
                .build();
    }

    public City toCity() {
        var city = new City();
        city.setId(id);
        city.setName(name);
        city.setInfo(info);
        city.setCountry(Objects.nonNull(country) ? country.toCountry() : null);
        return city;
    }
}
