package com.example.demo.controller;
import com.example.demo.service.UserService;

import jakarta.validation.constraints.Min;

import com.example.demo.model.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    @Value("${app.message:Default message}")
    private String appMessage;

    public UserController(UserService userService) {
        logger.info("UserController initialized with UserService");
        this.userService = userService;
    }

    @GetMapping("/message")
    public String getMessage() {
        return appMessage;
    }

    @RequestMapping
    public List<User> getAll(@RequestParam(defaultValue = "10") @Min(value = 1, message = "Limit must be at least 1") int limit) {
        logger.info("Fetching first {} users", limit);
        return userService.getAllUsers(limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public User create(@RequestBody User user) {
        logger.info("Creating new user: {}", user);
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        logger.info("Updating user with id: {}", id);
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting user with id: {}", id);
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
