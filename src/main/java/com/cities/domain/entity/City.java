package com.cities.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class City {
    private final String id;
    private final String name;
    private final String photo;
}
