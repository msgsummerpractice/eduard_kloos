package com.example.demo;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.demo.model.User;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl usersService;

    @Test
    void shouldReturnUsers() {
        List<User> users = List.of(
            new User(1L, "John Doe1", "john.doe1@email.com", "password123"),
            new User(2L, "John Doe2", "john.doe2@email.com", "password123"),
            new User(3L, "John Doe3", "john.doe3@email.com", "password123"),
            new User(4L, "John Doe4", "john.doe4@email.com", "password123")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = usersService.getAllUsers(10);

        assertEquals(4, result.size());

        verify(userRepository).findAll();
    }

    @Test 
    void shouldReturnEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = usersService.getAllUsers(10);

        assertEquals(0, result.size());

        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUserById() {
        User user = new User(1L, "John Doe", "john.doe@email.com", "password123");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User result = usersService.getUserById(1L);

        assertEquals(user, result);

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            usersService.getUserById(1L);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    void shouldCreateUser() {
        User user = new User(null, "John Doe", "john.doe@email.com", "password123");

        when(userRepository.save(user)).thenReturn(new User(1L, "John Doe", "john.doe@email.com", "password123"));

        User result = usersService.createUser(user);

        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@email.com", result.getEmail());
        assertEquals("password123", result.getPassword());

        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateUser() {
        User existingUser = new User(1L, "John Doe", "john.doe@email.com", "password123");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = usersService.updateUser(1L, existingUser);

        assertEquals(existingUser, result);

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        // do nothing when deleteById is called

        boolean result = usersService.deleteUser(1L);

        assertEquals(true, result);

        verify(userRepository).deleteById(1L);
    }
}