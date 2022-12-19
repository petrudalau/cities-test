package com.cities.adapter.out.repository;

import com.cities.adapter.out.repository.mongo.MongoCityRepository;
import com.cities.adapter.out.repository.mongo.model.CityEntity;
import com.cities.domain.entity.City;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.valueobject.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoSpringDataCityRepositoryTest {
    @Mock
    private MongoCityRepository springDataRepo;
    private MongoSpringDataCityRepository repository;

    @Before
    public void init() {
        repository = new MongoSpringDataCityRepository(springDataRepo);
    }

    @Test
    public void getCityById() throws GenericCityException {
        CityEntity entity = CityEntity.builder().id("1").name("city1").photo("url1").build();
        when(springDataRepo.findById("1")).thenReturn(Optional.of(entity));
        City result = repository.getCityById("1");
        assertCity(entity, result);
    }

    @Test
    public void getCityById_notFound() throws GenericCityException {
        when(springDataRepo.findById("1")).thenReturn(Optional.empty());
        City result = repository.getCityById("1");
        assertNull(result);
    }

    @Test(expected = GenericCityException.class)
    public void getCityById_exception() throws GenericCityException {
        when(springDataRepo.findById("1")).thenThrow(new InvalidDataAccessApiUsageException("error"));
        repository.getCityById("1");
    }

    @Test
    public void existsById() throws GenericCityException {
        when(springDataRepo.existsById("1")).thenReturn(true);
        boolean result = repository.existsById("1");
        assertTrue(result);
    }

    @Test(expected = GenericCityException.class)
    public void existsById_exception() throws GenericCityException {
        when(springDataRepo.existsById("1")).thenThrow(new InvalidDataAccessApiUsageException("error"));
        repository.existsById("1");
    }

    @Test
    public void updateCity() throws GenericCityException {
        CityEntity entity = CityEntity.builder().id("1").name("city1").photo("url1").build();
        when(springDataRepo.save(eq(entity))).thenReturn(entity);
        City result = repository.updateCity(City.builder().id("1").name("city1").photo("url1").build(), "user");
        assertCity(entity, result);
        assertEquals(entity.getLastUpdateBy(), "user");
        assertNotNull(entity.getLastUpdateTime());
    }

    @Test(expected = GenericCityException.class)
    public void updateCity_exception() throws GenericCityException {
        CityEntity entity = CityEntity.builder().id("1").name("city1").photo("url1").build();
        when(springDataRepo.save(eq(entity))).thenThrow(new InvalidDataAccessApiUsageException("error"));
        repository.updateCity(City.builder().id("1").name("city1").photo("url1").build(), "user");
    }

    @Test
    public void search() throws GenericCityException {
        List<CityEntity> entities = Arrays.asList(CityEntity.builder().id("1").name("city1").photo("url1").build(),
                CityEntity.builder().id("2").name("city2").photo("url2").build());
        when(springDataRepo.findAll(eq(PageRequest.of(3, 25, Sort.by("photo").descending())))).thenReturn(new PageImpl<>(entities));
        List<City> result = repository.search(SearchCriteria.builder().sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build());
        assertCities(entities, result);
    }

    @Test
    public void search_byName() throws GenericCityException {
        List<CityEntity> entities = Arrays.asList(CityEntity.builder().id("1").name("city1").photo("url1").build(),
                CityEntity.builder().id("2").name("city2").photo("url2").build());
        when(springDataRepo.findAllByNameContainingIgnoreCase(eq("city"), eq(PageRequest.of(3, 25, Sort.by("photo").descending())))).thenReturn(entities);
        List<City> result = repository.search(SearchCriteria.builder().name("city").sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build());
        assertCities(entities, result);
    }

    @Test(expected = GenericCityException.class)
    public void search_exception() throws GenericCityException {
        when(springDataRepo.findAllByNameContainingIgnoreCase(eq("city"), eq(PageRequest.of(3, 25, Sort.by("photo").descending()))))
                .thenThrow(new InvalidDataAccessApiUsageException("error"));
        repository.search(SearchCriteria.builder().name("city").sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build());
    }

    @Test
    public void count() throws GenericCityException {
        when(springDataRepo.count()).thenReturn(Long.valueOf(123));
        Long result = repository.count(null);
        assertEquals(Long.valueOf(123), result);
    }

    @Test
    public void count_byName() throws GenericCityException {
        when(springDataRepo.countByNameContainingIgnoreCase("city")).thenReturn(Long.valueOf(123));
        Long result = repository.count("city");
        assertEquals(Long.valueOf(123), result);
    }

    @Test(expected = GenericCityException.class)
    public void count_exception() throws GenericCityException {
        when(springDataRepo.count()) .thenThrow(new InvalidDataAccessApiUsageException("error"));
        repository.count(null);
    }

    private void assertCity(CityEntity entity, City city) {
        assertEquals(entity.getId(), city.getId());
        assertEquals(entity.getName(), city.getName());
        assertEquals(entity.getPhoto(), city.getPhoto());
    }

    private void assertCities(List<CityEntity> entities, List<City> cities) {
        assertEquals(entities.size(), cities.size());
        for( int i = 0; i < entities.size(); i++){
            assertCity(entities.get(i), cities.get(i));
        }
    }
}
