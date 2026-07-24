package com.example.demo;


import com.example.demo.model.User;
import com.example.demo.repository.InMemoryUserRepository;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserRepositoryTest {

    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    public void shouldSaveAndFindUsers() {
        User user = new User(null, "Jane Doe", "jane.doe@email.com", "password123");
        userRepository.save(user);

        List<User> users = userRepository.findAll(10);
        assertEquals(5, users.size());
         assertTrue(users.stream()
                        .anyMatch(u -> u.getName().equals("Jane Doe"))
        );
    }
}
