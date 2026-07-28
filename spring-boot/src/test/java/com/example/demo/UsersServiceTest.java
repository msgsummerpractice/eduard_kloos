package com.example.demo;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.dto.PatchUserRequest;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
@Transactional
public class UsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl usersService;

    @Test
    void shouldReturnUsers() {

        List<User> users = List.of(
                new User(1L, "John Doe1", "john.doe1@email.com", "password123", null),
                new User(2L, "John Doe2", "john.doe2@email.com", "password123", null),
                new User(3L, "John Doe3", "john.doe3@email.com", "password123", null),
                new User(4L, "John Doe4", "john.doe4@email.com", "password123", null));

        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<User> result = usersService.getAllUsers(0, 10);

        assertEquals(4, result.getSize());

        verify(userRepository)
                .findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {

        Page<User> page = new PageImpl<>(List.of());

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<User> result = usersService.getAllUsers(0, 10);

        assertEquals(0, result.getSize());

        verify(userRepository)
                .findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnUserById() {
        User user = new User(1L, "John Doe", "john.doe@email.com", "password123", null);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User result = usersService.getUserById(1L);

        assertEquals(user, result);

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usersService.getUserById(1L));

        assertEquals(
                "User with id 1 not found",
                exception.getMessage());
    }

    @Test
    void shouldCreateUser() {
        User user = new User(null, "John Doe", "john.doe@email.com", "password123", null);

        when(userRepository.save(any(User.class)))
                .thenReturn(
                        new User(
                                1L,
                                "John Doe",
                                "john.doe@email.com",
                                "password123",
                                null));

        User result = usersService.createUser(user);

        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@email.com", result.getEmail());
        assertEquals("password123", result.getPassword());

        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateUser() {

        User existingUser = new User(
                1L,
                "John Doe",
                "john.doe@email.com",
                "oldpassword",
                null);

        User updateData = new User(
                null,
                "Updated John",
                "updated.email@email.com",
                "newpassword123",
                null);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode("newpassword123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        User result = usersService.updateUser(1L, updateData);

        assertEquals(1L, result.getId());
        assertEquals("Updated John", result.getName());
        assertEquals("updated.email@email.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository)
                .findById(1L);

        verify(passwordEncoder)
                .encode("newpassword123");

        verify(userRepository)
                .save(existingUser);
    }

    @Test
    void shouldDeleteUser() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        usersService.deleteUser(1L);

        verify(userRepository)
                .existsById(1L);

        verify(userRepository)
                .deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteUserNotFound() {

        when(userRepository.existsById(1L))
                .thenReturn(false);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> usersService.deleteUser(1L));

        assertEquals(
                "User not found",
                exception.getMessage());

        verify(userRepository)
                .existsById(1L);
    }

    @Test
    void shouldFindUserByName() {
        User user = new User(
                1L,
                "John Doe",
                "john.doe@email.com",
                "password123",
                null);

        when(userRepository.findByName("John Doe"))
                .thenReturn(Optional.of(user));

        User result = usersService.findByName("John Doe");

        assertEquals("John Doe", result.getName());

        verify(userRepository)
                .findByName("John Doe");
    }

    @Test
    void shouldThrowExceptionWhenNameNotFound() {

        when(userRepository.findByName("Unknown"))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> usersService.findByName("Unknown"));

        assertEquals(
                "User with name Unknown not found",
                exception.getMessage());

        verify(userRepository)
                .findByName("Unknown");
    }

    @Test
    void shouldFindUserByEmail() {

        User user = new User(
                1L,
                "John Doe",
                "john.doe@email.com",
                "password123",
                null);

        when(userRepository.findByEmail("john.doe@email.com"))
                .thenReturn(Optional.of(user));

        User result = usersService.findByEmail("john.doe@email.com");

        assertEquals(
                "john.doe@email.com",
                result.getEmail());

        verify(userRepository)
                .findByEmail("john.doe@email.com");
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {

        when(userRepository.findByEmail("missing@email.com"))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> usersService.findByEmail("missing@email.com"));

        assertEquals(
                "User with email missing@email.com not found",
                exception.getMessage());

        verify(userRepository)
                .findByEmail("missing@email.com");
    }

    @Test
    void shouldPatchUser() {

        User existingUser = new User(
                1L,
                "John Doe",
                "john@email.com",
                "password123",
                null);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        PatchUserRequest request = new PatchUserRequest();
        request.setName("Updated Name");

        User result = usersService.patchUser(1L, request);

        assertEquals(
                "Updated Name",
                result.getName());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }
}