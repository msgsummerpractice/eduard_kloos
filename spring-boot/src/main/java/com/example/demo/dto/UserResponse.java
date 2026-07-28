package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter
@AllArgsConstructor
@Schema(description = "Response returned after creating or retrieving a user")
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John Doe")
    private String name;

    @Schema(example = "john.doe@example.com")
    private String email;
}