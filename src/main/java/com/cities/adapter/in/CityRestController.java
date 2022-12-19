package com.cities.adapter.in;

import com.cities.adapter.in.mapper.CityMapper;
import com.cities.domain.CityAggregate;
import com.cities.domain.entity.City;
import com.cities.domain.exception.CityNotFoundException;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.valueobject.SearchCriteria;
import com.cities.adapter.in.dto.CityDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cities")
@AllArgsConstructor
@Slf4j
public class CityRestController {
    private final CityAggregate cityAggregate;

    @GetMapping
    public ResponseEntity<List<CityDto>> getCities(@RequestParam(value = "sortBy", required = false) String sortBy,
                                                                                        @RequestParam(value = "sortOrder", required = false) String sortOrder,
                                                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                                        @RequestParam(value = "startPage", required = false) Integer startPage) {
        try {
            List<City> cities = cityAggregate.search(initSearcCriteria(null, sortBy, sortOrder, pageSize, startPage));
            return ResponseEntity.ok(cities.stream().map(CityMapper::toCityDto).collect(Collectors.toList()));
        } catch (GenericCityException e) {
            log.error("Error occurred while loading cities list.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SearchCriteria initSearcCriteria(String name, String sortBy, String sortOrder, Integer pageSize, Integer startPage) {
        return SearchCriteria.builder()
                .name(name)
                .sortBy(Objects.requireNonNullElse(sortBy, "name"))
                .sortOrder(Objects.requireNonNullElse(sortOrder, "ascending"))
                .pageSize(Objects.requireNonNullElse(pageSize, 10))
                .startPage(Objects.requireNonNullElse(startPage, 0))
                .build();
    }

    @GetMapping("/filter/{name}")
    public ResponseEntity<List<CityDto>> getByName(@PathVariable String name,
                                                   @RequestParam(value = "sortBy", required = false) String sortBy,
                                                   @RequestParam(value = "sortOrder", required = false) String sortOrder,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "startPage", required = false) Integer startPage) {
        try{
            List<City> cities = cityAggregate.search(initSearcCriteria(name, sortBy, sortOrder, pageSize, startPage));
            return ResponseEntity.ok(cities.stream().map(CityMapper::toCityDto).collect(Collectors.toList()));
        } catch (GenericCityException e) {
            log.error("Error occurred while loading cities list with corresponding name.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/count/{name}")
    public ResponseEntity<Long> countByName(@PathVariable String name) {
        try{
            return ResponseEntity.ok(cityAggregate.count(name));
        } catch (GenericCityException e) {
            log.error("Error occurred while counting cities with corresponding name.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        try{
            return ResponseEntity.ok(cityAggregate.count(null));
        } catch (GenericCityException e) {
            log.error("Error occurred while counting cities.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ALLOW_EDIT')")
    @PutMapping("/{id}")
    public ResponseEntity<CityDto> putById(@RequestBody CityDto city, Principal principal) {
        try {
            City result = cityAggregate.updateCity(CityMapper.toCity(city), principal.getName());
            return ResponseEntity.ok(CityMapper.toCityDto(result));
        } catch (GenericCityException e) {
            log.error("Error occurred while updating city.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CityNotFoundException e) {
            log.error("City not found.", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
