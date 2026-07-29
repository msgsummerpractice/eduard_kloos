package com.example.demo.service;

import com.example.demo.dto.PatchUserRequest;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import java.util.List;

public interface UserService {
    Page<User> getAllUsers(int page, int size);

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User patchUser(Long id, PatchUserRequest request);

    User findByName(String name);

    User findByEmail(String email);

    List<User> searchByName(String name);

    List<User> searchByEmail(String email);

    List<User> searchTop10Users(String name);

    long countUsers();
}