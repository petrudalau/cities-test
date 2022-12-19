package com.cities.boot.config;

import com.cities.adapter.out.repository.MongoSpringDataCityRepository;
import com.cities.adapter.out.repository.mongo.MongoCityRepository;
import com.cities.domain.repository.CityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@EnableMongoRepositories(basePackages = "com.cities.adapter.out.repository.mongo")
public class CityRepositoryConfig {
    @Bean
    public CityRepository cityRepository(
            MongoCityRepository mongoRepository) {
        return new MongoSpringDataCityRepository(mongoRepository);
    }

    // FIXME should not execute everytime
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getRespositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{new ClassPathResource("cities-initial-data.json")});
        return factory;
    }
}
