package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;
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
            .findAll(limit)
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
    public User createUser(User user) {
        logger.info("Creating new user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        return userRepository.deleteById(id);
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
    
}
