package com.telegram.bot.web.controller;

import com.telegram.bot.service.CountryService;
import com.telegram.bot.web.dto.CountryDTO;
import com.telegram.bot.web.validator.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/counties")
@Api(tags = "County-controller")
public class CountryController extends BaseController {

    private static final List<String> COUNTRY_FIELDS = List.of("id", "name");

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "Get county", value = "Returns counties with given id",
            notes = "Get country."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>country id failed validation</td></tr>"
                    + "<tr><td>BeanNotFoundException</td><td>country with given id not found</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public CountryDTO getCountry(@ApiParam(value = "Country id", required = true) @PathVariable Integer id) {
        ValidatorUtil.validateId(id);
        return CountryDTO.of(countryService.getCountryById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "Get countries", value = "Returns countries",
            notes = "Get countries."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>pagination or sort params failed validation</td></tr>"
                    + "<tr><td>BeanNotFoundException</td><td>countries not found</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public List<CountryDTO> getAllCountries(
            @ApiParam(value = "Page", required = true) @RequestParam Integer page,
            @ApiParam(value = "Size", required = true) @RequestParam Integer size,
            @ApiParam(value = "Sort type [asc, desc]", required = true) @RequestParam String sortType,
            @ApiParam(value = "Sort by [id, name]", required = true) @RequestParam String sortBy) {
        ValidatorUtil.validatePagination(page, size);
        ValidatorUtil.validateSortWithFields(sortType, sortBy, COUNTRY_FIELDS);
        return countryService.getAllCountries(page, size, sortType, sortBy)
                .stream()
                .map(CountryDTO::of)
                .collect(Collectors.toList());
    }
}
