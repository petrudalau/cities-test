package com.cities.adapter.out.mapper;

import com.cities.domain.entity.City;
import com.cities.adapter.out.repository.mongo.model.CityEntity;

public class CityMapper {
    public static City toCity(CityEntity entity) {
        return City.builder()
                .id(entity.getId())
                .name(entity.getName())
                .photo(entity.getPhoto())
                .build();
    }

    public static CityEntity toCityEntity(City entity) {
        return CityEntity.builder()
                .id(entity.getId())
                .name(entity.getName())
                .photo(entity.getPhoto())
                .build();
    }
}
