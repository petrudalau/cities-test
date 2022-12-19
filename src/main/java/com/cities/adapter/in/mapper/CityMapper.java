package com.cities.adapter.in.mapper;

import com.cities.domain.entity.City;
import com.cities.adapter.in.dto.CityDto;

public class CityMapper {
    public static City toCity(CityDto entity) {
        return City.builder()
                .id(entity.getId())
                .name(entity.getName())
                .photo(entity.getPhoto())
                .build();
    }

    public static CityDto toCityDto(City entity) {
        return CityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .photo(entity.getPhoto())
                .build();
    }
}
