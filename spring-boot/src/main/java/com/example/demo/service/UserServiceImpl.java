package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;

import com.example.demo.dto.PatchUserRequest;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getAllUsers(int page, int size) {
        logger.info("Fetching page {} of users with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User createUser(@NonNull User user) {
        logger.info("Creating new user: {}", user);
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(@NonNull Long id) {
        logger.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        logger.info("Updating user with id: {}", id);
        User existing = getUserById(id);
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(
                passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existing);
    }

    @Override
    public User patchUser(Long id, PatchUserRequest request) {
        logger.info("Patching user with id: {}", id);

        User user = getUserById(id);

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public User findByName(String name) {
        logger.info("Searching user by name: {}", name);

        return userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with name " + name + " not found"));
    }

    @Override
    public User findByEmail(String email) {
        logger.info("Searching user by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
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

    @Override
    public List<User> searchTop10Users(String name) {

        logger.info("Searching top 10 users by name: {}", name);

        return userRepository
                .findTop10ByNameContainingIgnoreCaseOrderByNameAsc(name);
    }

    @Override
    public long countUsers() {

        logger.info("Counting users");

        return userRepository.countUsers();
    }
}
