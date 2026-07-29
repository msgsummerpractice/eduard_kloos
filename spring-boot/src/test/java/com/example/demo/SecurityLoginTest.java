package com.example.demo;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class SecurityLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldDisplayLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldLoginWithValidUser() throws Exception {
        mockMvc.perform(
                formLogin("/login")
                        .user("john")
                        .password("password123"))
                .andExpect(
                        status().isOk());
    }

    @Test
    void shouldRejectInvalidLogin() throws Exception {
        mockMvc.perform(
                formLogin("/login")
                        .user("john")
                        .password("wrongpassword"))
                .andExpect(
                        status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "sam", roles = { "ADMIN" })
    void adminShouldAccessProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

    }
}
