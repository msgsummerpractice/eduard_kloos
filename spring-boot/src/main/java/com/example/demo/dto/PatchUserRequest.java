package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request used for partial user update")
public class PatchUserRequest {

    @Schema(example = "John Doe")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters long")
    private String name;

    @Schema(example = "john@example.com")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(example = "newPassword123")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long")
    private String password;

}
