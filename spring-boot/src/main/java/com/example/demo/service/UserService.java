package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers(int limit);
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    boolean deleteUser(Long id);
    User findByName(String name);
    User findByEmail(String email);
    List<User> searchByName(String name);
    List<User> searchByEmail(String email);
}