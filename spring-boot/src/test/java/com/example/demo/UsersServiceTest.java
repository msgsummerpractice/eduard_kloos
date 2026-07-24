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
import com.example.demo.repository.InMemoryUserRepository;
import com.example.demo.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    
    @Mock
    private InMemoryUserRepository userRepository;

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

        when(userRepository.findAll(10)).thenReturn(users);

        List<User> result = usersService.getAllUsers(10);

        assertEquals(4, result.size());

        verify(userRepository).findAll(10);
    }


}
    