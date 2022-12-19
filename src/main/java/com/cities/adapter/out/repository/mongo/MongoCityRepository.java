package com.cities.adapter.out.repository.mongo;

import com.cities.adapter.out.repository.mongo.model.CityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoCityRepository extends MongoRepository<CityEntity, String> {
    List<CityEntity> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Long countByNameContainingIgnoreCase(String name);
}
