package com.example.demo.controller;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import com.example.demo.config.AppProperties;
import com.example.demo.dto.PatchUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/users")
@Validated
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    private final AppProperties appProperties;

    private final UserMapper userMapper;

    public UserController(UserService userService, AppProperties appProperties, UserMapper userMapper) {
        logger.info("UserController initialized with UserService");
        this.userService = userService;
        this.appProperties = appProperties;
        this.userMapper = userMapper;
    }

    @Operation(
        summary = "Get application message",
        description = "Returns configured application message"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Message returned successfully"
    )
    @GetMapping("/message")
    public String getMessage() {
        return appProperties.getMessage();
    }

    @Operation(
        summary = "Get all users",
        description = "Returns a paginated list of users"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(
        produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE
        }
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAll(
        @RequestParam(defaultValue = "0")
        @Min(value = 0, message = "Page must be 0 or greater")
        int page,

        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "Size must be at least 1")
        int size
    ) {

        logger.info("Fetching page {} of users with size {}", page, size);

        Page<UserResponse> users = userService.getAllUsers(page, size)
            .map(userMapper::toResponse);

        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Returns a single user based on the provided ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping(
        value = "/{id}",
        produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE
        }
    )
    public ResponseEntity<UserResponse> getById(
    @Parameter(
        description = "ID of the user",
        example = "1"
    )
    @PathVariable Long id
    ) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(
                userMapper.toResponse(user)
        );
    }

    @Operation(summary = "Create a new user")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User creation data",
            required = true
        )
        @Valid @RequestBody UserRequest request
    ) {

        User user = userMapper.toEntity(request);

        User savedUser = userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toResponse(savedUser));
    }


    @Operation(
        summary = "Update user",
        description = "Updates an existing user by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
        @Parameter(
                description = "ID of the user to update",
                example = "1"
        )
        @PathVariable Long id,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated user information",
                required = true
        )
        @Valid @RequestBody UpdateUserRequest request
    ) {

        User user = userMapper.toEntity(request);


        User updated = userService.updateUser(id, user);


        return ResponseEntity.ok(
                userMapper.toResponse(updated)
        );
    }

    @Operation(summary = "Delete a user")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        logger.info("Deleting user with id: {}", id);

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Partially update user",
        description = "Updates one or more fields of an existing user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patch(
            @Parameter(
                    description = "ID of the user to update",
                    example = "1"
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fields to update",
                    required = true
            )
            @Valid @RequestBody PatchUserRequest request
    ) {

        logger.info("Partially updating user with id: {}", id);

        User updated = userService.patchUser(id, request);

        return ResponseEntity.ok(
                userMapper.toResponse(updated)
        );
    }

    @Operation(
        summary = "Find user by name",
        description = "Returns a user based on the provided name"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<UserResponse> findByName(
        @Parameter(
                description = "Name of the user",
                example = "John"
        ) 
        @PathVariable String name) {

        User user = userService.findByName(name);

            return ResponseEntity.ok(
                    userMapper.toResponse(user)
            );
    }


    @Operation(
        summary = "Find user by email",
        description = "Returns a user based on the provided email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(
        @Parameter(
                description = "Email address of the user",
                example = "john@example.com"
        )
        @PathVariable String email
    ) {

        User user = userService.findByEmail(email);

            return ResponseEntity.ok(
                    userMapper.toResponse(user)
            );
    }
}
