package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MfaVerifyRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignInResponse;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MfaService mfaService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            MfaService mfaService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mfaService = mfaService;
    }

    @Override
    public SignInResponse login(SignInRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        mfaService.generateCode(user.getEmail());
        return new SignInResponse(null, user.getRoles().stream().map(role -> role.getName()).toList(), true);
    }

    @Override
    public SignInResponse verifyMfa(MfaVerifyRequest request) {

        boolean valid = mfaService.verifyCode(
                request.getEmail(),
                request.getCode());
        if (!valid) {
            throw new RuntimeException("Invalid MFA code");
        }
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("User not found"));
        var token = jwtService.generateToken(user.getEmail());
        return new SignInResponse(
                token,
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .toList(),
                false);
    }
}