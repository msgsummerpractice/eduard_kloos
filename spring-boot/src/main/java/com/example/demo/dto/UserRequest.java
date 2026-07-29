package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request used to create a user")
public class UserRequest {

    @Schema(description = "User name", example = "John Doe")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 8, message = "Name must be at least 8 characters long")
    private String name;

    @Schema(description = "User email", example = "john.doe@example.com")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User password", example = "password123")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}