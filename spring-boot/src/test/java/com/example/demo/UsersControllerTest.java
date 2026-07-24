package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUsersEndpoint() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"John Doe1\"},{\"id\":2,\"name\":\"John Doe2\"},{\"id\":3,\"name\":\"John Doe3\"},{\"id\":4,\"name\":\"John Doe4\"}]"))
                .andExpect(header().string("Content-Type", "application/json"));
    }

}
