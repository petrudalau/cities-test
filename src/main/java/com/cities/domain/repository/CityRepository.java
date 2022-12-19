package com.cities.domain.repository;

import com.cities.domain.entity.City;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.valueobject.SearchCriteria;

import java.util.List;

public interface CityRepository {
    City getCityById(String id) throws GenericCityException;

    boolean existsById(String id) throws GenericCityException;

    City updateCity(City city) throws GenericCityException;
    List<City> search(SearchCriteria searchCriteria) throws GenericCityException;

    Long count(String name) throws GenericCityException;
}
