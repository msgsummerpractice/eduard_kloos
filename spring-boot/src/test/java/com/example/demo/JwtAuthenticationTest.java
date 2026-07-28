package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class JwtAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String loginAndGetToken() throws Exception {

        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"sam@test.com",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json
                .get("token")
                .asText();
    }

    @Test
    public void testLoginReturnsJwtToken() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"sam@test.com",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.roles[0]")
                        .value("ADMIN"));

    }

    @Test
    public void testAccessUsersEndpointWithJwt() throws Exception {

        String token = loginAndGetToken();

        mockMvc.perform(get("/api/users")
                .header(
                    "Authorization",
                    "Bearer " + token
                )
                .param("page","0")
                .param("size","10"))
                .andExpect(status().isOk());

    }

    @Test
    public void testAccessUsersEndpointWithoutJwt() throws Exception {

        mockMvc.perform(get("/api/users")
                .param("page","0")
                .param("size","10"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testInvalidJwtToken() throws Exception {

        mockMvc.perform(get("/api/users")
                .header(
                    "Authorization",
                    "Bearer invalid-token"
                )
                .param("page","0")
                .param("size","10"))
                .andExpect(status().isUnauthorized());

    }

}