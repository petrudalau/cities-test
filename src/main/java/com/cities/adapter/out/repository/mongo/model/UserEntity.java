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
@Document(collection = UserEntity.COLLECTION_NAME_USERS)
public class UserEntity {
    public static final String COLLECTION_NAME_USERS = "users";
    @Id
    private String id;
    @Indexed
    private String name;
    private String photo;
    private Instant lastUpdateTime;
    private String lastUpdateBy;
}
