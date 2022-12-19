package com.cities.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class City {
    private final String id;
    private final String name;
    private final String photo;
    private final Instant lastUpdateTime;
    private final String lastUpdateBy;
}
