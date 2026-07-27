package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUsersEndpoint() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"John Doe1\"},{\"id\":2,\"name\":\"John Doe2\"},{\"id\":3,\"name\":\"John Doe3\"},{\"id\":4,\"name\":\"John Doe4\"}]"))
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    public void testGetUserByIdEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"John Doe1\",\"email\":\"john.doe1@email.com\",\"password\":\"password123\"}"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUsersWithInvalidLimit() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindUserByNameEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/name/John Doe1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":1,\"name\":\"John Doe1\",\"email\":\"john.doe1@email.com\",\"password\":\"password123\"}"
                ))
                .andExpect(header().string("Content-Type", "application/json"));
    }


    @Test
    public void testFindUserByNameNotFound() throws Exception {
        mockMvc.perform(get("/api/users/name/Unknown"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testFindUserByEmailEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/email/john.doe1@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":1,\"name\":\"John Doe1\",\"email\":\"john.doe1@email.com\",\"password\":\"password123\"}"
                ))
                .andExpect(header().string("Content-Type", "application/json"));
    }


    @Test
    public void testFindUserByEmailNotFound() throws Exception {
        mockMvc.perform(get("/api/users/email/notfound@email.com"))
                .andExpect(status().isNotFound());
    }

	@Test
    public void testCreateUserEndpoint() throws Exception {

	mockMvc.perform(post("/api/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
					{
							"name": "New User",
							"email": "new@email.com",
							"password": "password123"
					}
					"""))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value("New User"));
	}

	@Test
	public void testUpdateUserEndpoint() throws Exception {

	mockMvc.perform(put("/api/users/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
					{
							"name": "Updated User",
							"email": "updated@email.com",
							"password": "password123"
					}
					"""))
			.andExpect(status().isOk());
	}

	@Test
	public void testDeleteUserEndpoint() throws Exception {

	mockMvc.perform(delete("/api/users/1"))
			.andExpect(status().isNoContent());
	}

	@Test
	public void testPatchUserEndpoint() throws Exception {

	mockMvc.perform(patch("/api/users/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
					{
							"name": "Patched Name"
					}
					"""))
			.andExpect(status().isOk());
	}

        
}
