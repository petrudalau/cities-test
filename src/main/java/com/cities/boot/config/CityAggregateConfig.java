package com.cities.boot.config;

import com.cities.domain.CityAggregate;
import com.cities.domain.aggregate.DefaultCityAggregate;
import com.cities.domain.repository.CityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CityAggregateConfig {
    @Bean
    public CityAggregate cityAggregate(
            CityRepository repository) {
        return new DefaultCityAggregate(repository);
    }
}
