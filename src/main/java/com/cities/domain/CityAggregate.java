package com.cities.domain;

import com.cities.domain.exception.CityNotFoundException;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.valueobject.SearchCriteria;
import com.cities.domain.entity.City;

import java.util.List;

public interface CityAggregate {
    City getCityById(String id) throws GenericCityException, CityNotFoundException;
    City updateCity(City city, String updatedBy) throws GenericCityException, CityNotFoundException;
    List<City> search(SearchCriteria searchCriteria) throws GenericCityException;
    Long count(String name) throws GenericCityException;
}
