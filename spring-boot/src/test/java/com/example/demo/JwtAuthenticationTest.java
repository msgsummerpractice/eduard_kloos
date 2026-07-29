package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.demo.service.MfaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

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

    @MockBean
    private MfaService mfaService;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        when(mfaService.verifyCode(
                "sam@test.com",
                "123456"))
                .thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"sam@test.com",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mfaRequired")
                        .value(true));

        String response = mockMvc.perform(post("/api/auth/verify-mfa")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"sam@test.com",
                            "code":"123456"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        this.token = json.get("token").asText();
    }

    @Test
    public void testLoginRequiresMfa() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"sam@test.com",
                            "password":"password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.mfaRequired").value(true))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
    }

    @Test
    public void testAccessUsersEndpointWithJwt() throws Exception {

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + this.token)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessUsersEndpointWithoutJwt() throws Exception {

        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testInvalidJwtToken() throws Exception {

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer invalid-token")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isUnauthorized());
    }

}