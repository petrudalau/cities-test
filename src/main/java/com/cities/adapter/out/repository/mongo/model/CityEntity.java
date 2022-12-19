package com.cities.adapter.out.repository.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = CityEntity.COLLECTION_NAME_CITIES)
public class CityEntity {
    public static final String COLLECTION_NAME_CITIES = "cities";
    @Id
    private String id;
    @Indexed
    private String name;
    private String photo;
    private Instant lastUpdateTime;
    private String lastUpdateBy;
}
