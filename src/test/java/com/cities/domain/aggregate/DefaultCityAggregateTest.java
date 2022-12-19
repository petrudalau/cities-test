package com.cities.domain.aggregate;

import com.cities.domain.entity.City;
import com.cities.domain.exception.CityNotFoundException;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.repository.CityRepository;
import com.cities.domain.valueobject.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCityAggregateTest {
    private static final String TEST_USER = "user_login";
    @Mock
    private CityRepository cityRepository;
    private DefaultCityAggregate aggregate;

    @Before
    public void init() {
        aggregate = new DefaultCityAggregate(cityRepository);
    }

    @Test
    public void getCityById() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city1").photo("url1").build();
        when(cityRepository.getCityById("1")).thenReturn(city);
        City result = aggregate.getCityById("1");
        assertSame(city, result);
    }

    @Test(expected = CityNotFoundException.class)
    public void getCityById_notFound() throws GenericCityException, CityNotFoundException {
        when(cityRepository.getCityById("1")).thenReturn(null);
        aggregate.getCityById("1");
    }

    @Test(expected = GenericCityException.class)
    public void getCityById_exception() throws GenericCityException, CityNotFoundException {
        when(cityRepository.getCityById("1")).thenThrow(new GenericCityException(new RuntimeException()));
        aggregate.getCityById("1");
    }

    @Test
    public void updateCity() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city1").photo("url1").build();
        when(cityRepository.existsById(eq("1"))).thenReturn(true);
        when(cityRepository.updateCity(eq(city), same(TEST_USER))).thenReturn(city);
        City result = aggregate.updateCity(city, TEST_USER);
        assertSame(city, result);
    }

    @Test(expected = CityNotFoundException.class)
    public void updateCity_notFound() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city1").photo("url1").build();
        when(cityRepository.existsById(eq("1"))).thenReturn(false);
        aggregate.updateCity(city, TEST_USER);
    }

    @Test(expected = GenericCityException.class)
    public void updateCity_exception() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city1").photo("url1").build();
        when(cityRepository.existsById(eq("1"))).thenReturn(true);
        when(cityRepository.updateCity(eq(city), same(TEST_USER))).thenThrow(new GenericCityException(new RuntimeException()));
        aggregate.updateCity(city,TEST_USER);
    }

    @Test
    public void search() throws GenericCityException {
        List<City> cities = Arrays.asList(City.builder().id("1").name("city1").photo("url1").build(),
                City.builder().id("2").name("city2").photo("url2").build());
        SearchCriteria searchCriteria = SearchCriteria.builder().name("city").sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build();
        when(cityRepository.search(eq(searchCriteria))).thenReturn(cities);
        List<City> result = aggregate.search(searchCriteria);
        assertSame(cities, result);
    }

    @Test(expected = GenericCityException.class)
    public void search_exception() throws GenericCityException {
        SearchCriteria searchCriteria = SearchCriteria.builder().name("city").sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build();
        when(cityRepository.search(eq(searchCriteria))).thenThrow(new GenericCityException(new RuntimeException()));
        aggregate.search(searchCriteria);
    }

    @Test
    public void count_byName() throws GenericCityException {
        when(cityRepository.count("city")).thenReturn(Long.valueOf(123));
        Long result = aggregate.count("city");
        assertEquals(Long.valueOf(123), result);
    }

    @Test(expected = GenericCityException.class)
    public void count_exception() throws GenericCityException {
        when(cityRepository.count(eq("city"))) .thenThrow(new GenericCityException(new RuntimeException()));
        aggregate.count("city");
    }
}
