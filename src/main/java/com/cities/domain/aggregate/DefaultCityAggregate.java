package com.cities.domain.aggregate;

import com.cities.domain.CityAggregate;
import com.cities.domain.entity.City;
import com.cities.domain.exception.CityNotFoundException;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.repository.CityRepository;
import com.cities.domain.valueobject.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class DefaultCityAggregate implements CityAggregate {
    private CityRepository cityRepository;

    @Override
    public City getCityById(String id) throws GenericCityException, CityNotFoundException {
        City city = cityRepository.getCityById(id);
        if (city == null) {
            log.error("City with id={} not found", id);
            throw new CityNotFoundException();
        }
        return city;
    }

    @Override
    public City updateCity(City city, String updatedBy) throws GenericCityException, CityNotFoundException {
        if (!cityRepository.existsById(city.getId())) {
            log.error("City with id={} not found", city.getId());
            throw new CityNotFoundException();
        }
        return cityRepository.updateCity(city, updatedBy);
    }

    @Override
    public List<City> search(SearchCriteria searchCriteria) throws GenericCityException {
        return cityRepository.search(searchCriteria);
    }

    @Override
    public Long count(String name) throws GenericCityException {
        return cityRepository.count(name);
    }
}
