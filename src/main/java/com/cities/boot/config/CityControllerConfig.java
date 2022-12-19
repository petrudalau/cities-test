package com.cities.boot.config;

import com.cities.domain.CityAggregate;
import com.cities.adapter.in.CityRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CityControllerConfig {
    @Bean
    public CityRestController cityController(
            CityAggregate aggregate) {
        return new CityRestController(aggregate);
    }
}
