package com.cities.adapter.out.repository;

import com.cities.adapter.out.mapper.CityMapper;
import com.cities.domain.entity.City;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.repository.CityRepository;
import com.cities.domain.valueobject.SearchCriteria;
import com.cities.adapter.out.repository.mongo.MongoCityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class MongoSpringDataCityRepository implements CityRepository {

    public static final String ASCENDING_ORDER = "ascending";
    private final MongoCityRepository mongoCityRepository;
    @Override
    public City getCityById(String id) throws GenericCityException {
        try {
            return mongoCityRepository.findById(id).map(CityMapper::toCity).orElse(null);
        } catch (Exception e) {
            log.error("Failed to read city by id={}.",id, e);
            throw new GenericCityException(e);
        }
    }

    @Override
    public boolean existsById(String id) throws GenericCityException {
        try {
            return mongoCityRepository.existsById(id);
        } catch (Exception e) {
            log.error("Failed to check city presence id={}.", id, e);
            throw new GenericCityException(e);
        }
    }

    @Override
    public City updateCity(City city) throws GenericCityException {
        try {
            return CityMapper.toCity(mongoCityRepository.save(CityMapper.toCityEntity(city)));
        } catch (Exception e) {
            log.error("Failed to update city.", e);
            throw new GenericCityException(e);
        }
    }

    @Override
    public List<City> search(SearchCriteria searchCriteria) throws GenericCityException {
        try {
            Sort sort = Sort.by(searchCriteria.getSortBy());
            sort = searchCriteria.getSortOrder().equalsIgnoreCase(ASCENDING_ORDER) ? sort.ascending() : sort.descending();
            PageRequest pageable = PageRequest.of(searchCriteria.getStartPage(), searchCriteria.getPageSize(), sort);
            if (StringUtils.hasText(searchCriteria.getName())) {
                return mongoCityRepository.findAllByNameContainingIgnoreCase(searchCriteria.getName(), pageable).stream().map(CityMapper::toCity).collect(Collectors.toList());
            } else {
                return mongoCityRepository.findAll(pageable).stream().map(CityMapper::toCity).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Failed to find cities with search criteria: ", searchCriteria, e);
            throw new GenericCityException(e);
        }
    }

    @Override
    public Long count(String name) throws GenericCityException {
        try {
            if (StringUtils.hasText(name)) {
                return mongoCityRepository.countByNameContainingIgnoreCase(name);
            } else {
                return mongoCityRepository.count();
            }
        } catch (Exception e) {
            log.error("Failed to count cities with name:{}", name, e);
            throw new GenericCityException(e);
        }
    }
}
