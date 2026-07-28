package com.example.demo.service;
import org.springframework.stereotype.Service;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignInResponse;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public SignInResponse login(SignInRequest request) {

        throw new UnsupportedOperationException("Not implemented yet");

    }
}