package com.example.demo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

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

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUsersEndpoint() throws Exception {

        mockMvc.perform(get("/api/users")
                .with(user("sam").password("password123").roles("ADMIN"))
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe1"))
                .andExpect(jsonPath("$.content[0].email").value("john.doe1@email.com"))
                .andExpect(jsonPath("$.content[3].id").value(4))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.totalPages").value(1));
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("User with id 999 not found"));
    }

    @Test
    public void testGetUsersWithInvalidSize() throws Exception {

        mockMvc.perform(get("/api/users")
                .with(user("sam").password("password123").roles("ADMIN"))
                .param("page", "0")
                .param("size", "0"))
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("User with name Unknown not found"));
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value(
                        "User with email notfound@email.com not found"
                        ));
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

        User user = userRepository.saveAndFlush(
        new User(
                null,
                "Delete User",
                "delete@email.com",
                "password123",
                null
        )
    );

        mockMvc.perform(delete("/api/users/" + user.getId()))
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

    @Test
    public void testPatchUserWithInvalidName() throws Exception {

        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name":"Jo"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchUserWithInvalidEmail() throws Exception {

        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"invalid-email"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchUserWithShortPassword() throws Exception {

        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "password":"123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithEmptyName() throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name":"",
                            "email":"test@email.com",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithoutEmail() throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name":"Test User",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithShortPassword() throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name":"Test User",
                            "email":"test@email.com",
                            "password":"123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithShortName() throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name":"Jo",
                            "email":"test@email.com",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithInvalidData() throws Exception {

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name":"",
                            "email":"test@email.com",
                            "password":"123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("User not found"));
    }

    @Test
    public void testGetUsersWithPagination() throws Exception {

        mockMvc.perform(get("/api/users")
                .with(user("sam").password("password123").roles("ADMIN"))
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }

}
