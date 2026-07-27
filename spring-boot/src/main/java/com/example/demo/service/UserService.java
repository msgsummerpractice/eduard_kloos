package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getAllUsers(int page, int size);
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    boolean deleteUser(Long id);
    User patchUser(Long id, Map<String, Object> updates);
    User findByName(String name);
    User findByEmail(String email);
    List<User> searchByName(String name);
    List<User> searchByEmail(String email);
    List<User> searchTop10Users(String username);
    long countUsers();
}