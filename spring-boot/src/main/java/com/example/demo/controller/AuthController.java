package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MfaVerifyRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignInResponse;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(
                authService.login(request));
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<SignInResponse> verifyMfa(@RequestBody MfaVerifyRequest request) {

        return ResponseEntity.ok(
                authService.verifyMfa(request));
    }

}
