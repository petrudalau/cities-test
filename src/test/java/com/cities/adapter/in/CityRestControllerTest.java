package com.cities.adapter.in;

import com.cities.adapter.in.dto.CityDto;
import com.cities.domain.CityAggregate;
import com.cities.domain.entity.City;
import com.cities.domain.exception.CityNotFoundException;
import com.cities.domain.exception.GenericCityException;
import com.cities.domain.valueobject.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityRestControllerTest {
    @Mock
    private CityAggregate cityAggregate;
    private CityRestController controller;

    @Before
    public void init() {
        controller = new CityRestController(cityAggregate);
    }

    @Test
    public void getCities() throws GenericCityException {
        List<City> cities = Arrays.asList(City.builder().id("1").name("city1").photo("url1").build(),
                City.builder().id("2").name("city2").photo("url2").build());
        when(cityAggregate.search(eq(SearchCriteria.builder().sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build()))).thenReturn(cities);
        ResponseEntity<List<CityDto>> result = controller.getCities("photo", "descending", 25, 3);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertCities(cities, result.getBody());
    }

    @Test
    public void getCities_defaultQuery() throws GenericCityException {
        List<City> cities = Arrays.asList(City.builder().id("1").name("city1").photo("url1").build(),
                City.builder().id("2").name("city2").photo("url2").build());
        when(cityAggregate.search(eq(SearchCriteria.builder().sortBy("name").sortOrder("ascending").pageSize(10).startPage(0).build()))).thenReturn(cities);
        ResponseEntity<List<CityDto>> result = controller.getCities(null, null, null, null);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertCities(cities, result.getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void getCities_exception() throws GenericCityException {
        when(cityAggregate.search(eq(SearchCriteria.builder().sortBy("name").sortOrder("ascending").pageSize(10).startPage(0).build())))
                .thenThrow(new GenericCityException(new RuntimeException()));
        controller.getCities(null, null, null, null);
    }

    @Test
    public void getByName() throws GenericCityException {
        List<City> cities = Arrays.asList(City.builder().id("1").name("city1").photo("url1").build(),
                City.builder().id("2").name("city2").photo("url2").build());
        when(cityAggregate.search(eq(SearchCriteria.builder().name("city").sortBy("photo").sortOrder("descending").pageSize(25).startPage(3).build()))).thenReturn(cities);
        ResponseEntity<List<CityDto>> result = controller.getByName("city", "photo", "descending", 25, 3);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertCities(cities, result.getBody());
    }

    @Test
    public void getByName_defaultQuery() throws GenericCityException {
        List<City> cities = Arrays.asList(City.builder().id("1").name("city1").photo("url1").build(),
                City.builder().id("2").name("city2").photo("url2").build());
        when(cityAggregate.search(eq(SearchCriteria.builder().name("city").sortBy("name").sortOrder("ascending").pageSize(10).startPage(0).build()))).thenReturn(cities);
        ResponseEntity<List<CityDto>> result = controller.getByName("city",null, null, null, null);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertCities(cities, result.getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void getByName_exception() throws GenericCityException {
        when(cityAggregate.search(eq(SearchCriteria.builder().name("city").sortBy("name").sortOrder("ascending").pageSize(10).startPage(0).build())))
                .thenThrow(new GenericCityException(new RuntimeException()));
        controller.getByName("city",null, null, null, null);
    }

    @Test
    public void count() throws GenericCityException {
        when(cityAggregate.count(isNull())).thenReturn(Long.valueOf(123));
        ResponseEntity<Long> result = controller.count();
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(Long.valueOf(123), result.getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void count_exception() throws GenericCityException {
        when(cityAggregate.count(isNull())).thenThrow(new GenericCityException(new RuntimeException()));
        controller.count();
    }

    @Test
    public void countByName() throws GenericCityException {
        when(cityAggregate.count(eq("city"))).thenReturn(Long.valueOf(123));
        ResponseEntity<Long> result = controller.countByName("city");
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(Long.valueOf(123), result.getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void countByName_exception() throws GenericCityException {
        when(cityAggregate.count(eq("city"))).thenThrow(new GenericCityException(new RuntimeException()));
        controller.countByName("city");
    }

    @Test
    public void putById() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city").photo("url").build();
        when(cityAggregate.updateCity(eq(city))).thenReturn(city);
        ResponseEntity<CityDto> result = controller.putById(new CityDto("1", "city", "url"));
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertCity(city, result.getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void putById_exception() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city").photo("url").build();
        when(cityAggregate.updateCity(eq(city))).thenThrow(new GenericCityException(new RuntimeException()));
        controller.putById(new CityDto("1", "city", "url"));
    }

    @Test(expected = ResponseStatusException.class)
    public void putById_notFound() throws GenericCityException, CityNotFoundException {
        City city = City.builder().id("1").name("city").photo("url").build();
        when(cityAggregate.updateCity(eq(city))).thenThrow(new CityNotFoundException());
        controller.putById(new CityDto("1", "city", "url"));
    }

    private void assertCity(City city, CityDto dto) {
        assertEquals(city.getId(), dto.getId());
        assertEquals(city.getName(), dto.getName());
        assertEquals(city.getPhoto(), dto.getPhoto());
    }

    private void assertCities(List<City> cities, List<CityDto> dtos) {
        assertEquals(cities.size(), dtos.size());
        for( int i = 0; i < cities.size(); i++){
            assertCity(cities.get(i), dtos.get(i));
        }
    }

}
