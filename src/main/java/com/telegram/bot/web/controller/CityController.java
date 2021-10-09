package com.telegram.bot.web.controller;

import com.telegram.bot.service.CityService;
import com.telegram.bot.web.dto.CityDTO;
import com.telegram.bot.web.validator.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/cities")
@Api(tags = "City-controller")
public class CityController extends BaseController {

    private static final List<String> CITY_FIELDS = List.of("id", "name", "country");

    private final CityService cityService;
    private final Validator cityValidator;
    private final Validator nullableCityValidator;

    public CityController(CityService cityService,
                          @Qualifier("cityValidator") Validator cityValidator,
                          @Qualifier("nullableCityValidator")Validator nullableCityValidator) {
        this.cityService = cityService;
        this.cityValidator = cityValidator;
        this.nullableCityValidator = nullableCityValidator;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "Get city", value = "Returns cities with given id",
            notes = "Get city."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>city id failed validation</td></tr>"
                    + "<tr><td>BeanNotFoundException</td><td>city with given id not found</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public CityDTO getCity(@ApiParam(value = "City id", required = true) @PathVariable Integer id) {
        ValidatorUtil.validateId(id);
        return CityDTO.of(cityService.getCityById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "Get cities", value = "Returns cities",
            notes = "Get cities."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>pagination or sort params failed validation</td></tr>"
                    + "<tr><td>BeanNotFoundException</td><td>cities not found</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public List<CityDTO> getAllCities(
            @ApiParam(value = "Page", required = true) @RequestParam Integer page,
            @ApiParam(value = "Size", required = true) @RequestParam Integer size,
            @ApiParam(value = "Sort type [asc, desc]", required = true) @RequestParam String sortType,
            @ApiParam(value = "Sort by [id, name, country]", required = true) @RequestParam String sortBy) {
        ValidatorUtil.validatePagination(page, size);
        ValidatorUtil.validateSortWithFields(sortType, sortBy, CITY_FIELDS);
        return cityService.getAllCities(page, size, sortType, sortBy)
                .stream()
                .map(CityDTO::of)
                .collect(Collectors.toList());
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(nickname = "Add city", value = "Add given city",
            notes = "Add city."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>city failed validation</td></tr>"
                    + "<tr><td>BeanCreationException</td><td>failed to add city</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public CityDTO addCity(@ApiParam(value = "City", required = true) @RequestBody CityDTO city) {
        validate(city, CITY, cityValidator);
        return CityDTO.of(cityService.addCity(city.toCity()));
    }

    @PostMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "Update city", value = "Update given city",
            notes = "Update city."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>city failed validation</td></tr>"
                    + "<tr><td>BeanModificationException</td><td>failed to update city</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public CityDTO updateCity(@ApiParam(value = "City", required = true) @RequestBody CityDTO city,
                                  @ApiParam(value = "City id", required = true) @PathVariable Integer id) {
        ValidatorUtil.validateId(id);
        validate(city, CITY, nullableCityValidator);
        return CityDTO.of(cityService.updateCity(city.toCity(), id));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "Delete city", value = "Delete given city",
            notes = "Delete city."
                    + "Possible error types:"
                    + "<table>"
                    + "<tbody>"
                    + "<tr><td>BeanValidationException</td><td>city failed validation</td></tr>"
                    + "<tr><td>BeanModificationException</td><td>failed to delete city</td></tr>"
                    + "</tbody>"
                    + "</table>")
    public void deleteStadium(@ApiParam(value = "City id", required = true) @PathVariable Integer id) {
        ValidatorUtil.validateId(id);
        cityService.deleteCity(id);
    }
}
