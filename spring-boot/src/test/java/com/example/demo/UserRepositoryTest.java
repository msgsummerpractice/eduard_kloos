package com.example.demo;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveAndFindUsers() {
        User user = new User(null, "Jane Doe", "jane.doe@email.com", "password123");
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
         assertTrue(users.stream()
                        .anyMatch(u -> u.getName().equals("Jane Doe"))
        );
    }

    @Test
    public void shouldDeleteUser() {
        User user = new User(null, "John", "john@email.com", "password123");
        userRepository.save(user);

        Long id = user.getId();

        userRepository.deleteById(id);

        assertFalse(userRepository.findById(id).isPresent());
    }

    @Test
    public void shouldFindUserById() {
        User user = new User(null, "John", "john@email.com", "password123");

        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId())
                .orElse(null);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getName());
    }
}
