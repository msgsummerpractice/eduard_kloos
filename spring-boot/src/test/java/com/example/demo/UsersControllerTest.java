package com.example.demo;
import jakarta.transaction.Transactional;
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
@Transactional
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUsersEndpoint() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"John Doe1\"},{\"id\":2,\"name\":\"John Doe2\"},{\"id\":3,\"name\":\"John Doe3\"},{\"id\":4,\"name\":\"John Doe4\"}]"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUserByIdEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"John Doe1\",\"email\":\"john.doe1@email.com\"}"))
                .andExpect(jsonPath("$.password").doesNotExist());
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
                        "{\"id\":1,\"name\":\"John Doe1\",\"email\":\"john.doe1@email.com\"}"
                ))
				.andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
                .andExpect(content().json("""
                        {
                            "id":1,
                            "name":"John Doe1",
                            "email":"john.doe1@email.com"
                        }
                        """))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
							"name":"New User",
							"email":"new@email.com",
							"password":"password123"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("New User"))
				.andExpect(jsonPath("$.email").value("new@email.com"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	public void testUpdateUserEndpoint() throws Exception {

		mockMvc.perform(put("/api/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
							"name":"Updated User",
							"email":"updated@email.com",
							"password":"password123"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated User"))
				.andExpect(jsonPath("$.email").value("updated@email.com"))
				.andExpect(jsonPath("$.password").doesNotExist());
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
							"name":"Patched Name"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Patched Name"))
				.andExpect(jsonPath("$.email").value("john.doe1@email.com"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

}
