package com.example.demo.controller;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import com.example.demo.config.AppProperties;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    private final AppProperties appProperties;

    public UserController(UserService userService, AppProperties appProperties) {
        logger.info("UserController initialized with UserService");
        this.userService = userService;
        this.appProperties = appProperties;
    }

    @GetMapping("/message")
    public String getMessage() {
        return appProperties.getMessage();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(@RequestParam(defaultValue = "10") @Min(value = 1, message = "Limit must be at least 1") int limit) {

        logger.info("Fetching first {} users", limit);

        List<UserResponse> users = userService.getAllUsers(limit)
                .stream()
                .map(UserMapper::toResponse)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {

        try {

            User user = userService.getUserById(id);

            return ResponseEntity.ok(
                    UserMapper.toResponse(user)
            );

        } catch(RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {

        User user = UserMapper.toEntity(request);

        User savedUser = userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toResponse(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());


        User updated = userService.updateUser(id, user);


        return ResponseEntity.ok(
                UserMapper.toResponse(updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        logger.info("Deleting user with id: {}", id);

        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {

        logger.info("Partially updating user with id: {}", id);

        try {

            User updated = userService.patchUser(id, updates);

            return ResponseEntity.ok(
                    UserMapper.toResponse(updated)
            );

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserResponse> findByName(@PathVariable String name) {

        try {

            User user = userService.findByName(name);

            return ResponseEntity.ok(
                    UserMapper.toResponse(user)
            );

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable String email) {

        try {

            User user = userService.findByEmail(email);

            return ResponseEntity.ok(
                    UserMapper.toResponse(user)
            );

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
