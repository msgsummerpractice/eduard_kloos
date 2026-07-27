package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

import io.micrometer.common.lang.NonNull;

import com.example.demo.model.User;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        logger.info("UserServiceImpl initialized with UserRepository");
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers(int limit) {
        logger.info("Fetching first {} users from the repository", limit);
        return userRepository
                .findAll()
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User createUser(@NonNull User user) {
        logger.info("Creating new user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(@NonNull Long id) {
    logger.info("Deleting user with id: {}", id);

    if (!userRepository.existsById(id)) {
        return false;
    }

    userRepository.deleteById(id);
    return true;
}

    @Override
    public User updateUser(Long id, User user) {
        logger.info("Updating user with id: {}", id);
        User existing = getUserById(id);
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        return userRepository.save(existing);
    }

    @Override
    public User findByName(String name) {
        logger.info("Searching user by name: {}", name);

        return userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public User findByEmail(String email) {
        logger.info("Searching user by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public List<User> searchByName(String name) {
        logger.info("Searching users containing name: {}", name);

        return userRepository.findByNameContainingIgnoreCase(name);
    }


    @Override
    public List<User> searchByEmail(String email) {
        logger.info("Searching users containing email: {}", email);

        return userRepository.findByEmailContainingIgnoreCase(email);
    }
    
}
