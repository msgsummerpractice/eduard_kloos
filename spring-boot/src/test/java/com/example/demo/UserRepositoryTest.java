package com.example.demo;

import jakarta.transaction.Transactional;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void shouldSaveAndFindUsers() {
        User user = new User(null, "Jane Doe", "jane.doe@email.com", "password123", null);
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertEquals(5, users.size());
         assertTrue(users.stream()
                        .anyMatch(u -> u.getName().equals("Jane Doe"))
        );
    }

    @Test
    public void shouldDeleteUser() {
        User user = new User(null, "John", "john@email.com", "password123", null);
        userRepository.save(user);

        Long id = user.getId();

        userRepository.deleteById(id);

        assertFalse(userRepository.findById(id).isPresent());
    }

    @Test
    public void shouldFindUserById() {
        User user = new User(null, "John", "john@email.com", "password123", null);

        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId())
                .orElse(null);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getName());
    }

    @Test
    void shouldFindTop10UsersByNameIgnoreCase() {

        List<User> users =
                userRepository.findTop10ByNameContainingIgnoreCaseOrderByNameAsc("john");


        assertEquals(4, users.size());

        assertEquals(
                "John Doe1",
                users.get(0).getName()
        );
    }


    @Test
    void shouldCountUsers() {

        long count = userRepository.countUsers();

        assertEquals(4, count);
    }
}
