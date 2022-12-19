package com.cities;

import com.cities.boot.CitiesApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataMongo
@AutoConfigureMockMvc
@SpringBootTest(
		properties = "de.flapdoodle.mongodb.embedded.version=5.0.5",
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = CitiesApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CitiesApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	@WithMockUser
	public void count() throws Exception {
		mvc.perform(get("/cities/count")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("1000"));
	}

	@Test
	@WithMockUser
	public void count_byName() throws Exception {
		mvc.perform(get("/cities/count/min")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("9"));
	}

	@Test
	public void count_unauthorized() throws Exception {
		mvc.perform(get("/cities/count/min")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void search() throws Exception {
		mvc.perform(get("/cities?sortBy=name&sortOrder=ascending&pageSize=12&startPage=0")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(12)));
	}

	@Test
	@WithMockUser
	public void search_byName() throws Exception {
		mvc.perform(get("/cities/filter/dor")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(6)));
	}

	@Test
	public void search_unauthorized() throws Exception {
		mvc.perform(get("/cities")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ALLOW_EDIT")
	public void update() throws Exception {
		mvc.perform(get("/cities/filter/test")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
		mvc.perform(put("/cities/1")
						.contentType(MediaType.APPLICATION_JSON).content("{\"id\": 1,\"name\": \"Test\",\"photo\": \"url\"}"))
				.andExpect(status().isOk());
		mvc.perform(get("/cities/filter/test")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is("1")));
	}

	@Test
	@WithMockUser
	public void update_unauthorized() throws Exception {
		mvc.perform(put("/cities/1")
						.contentType(MediaType.APPLICATION_JSON).content("{\"id\": 1,\"name\": \"Test\",\"photo\": \"url\"}"))
				.andExpect(status().isForbidden());
	}
}
