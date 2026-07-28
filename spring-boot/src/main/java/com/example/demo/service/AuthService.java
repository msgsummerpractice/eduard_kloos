package com.example.demo.service;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignInResponse;

public interface AuthService {

    SignInResponse login(SignInRequest request);

}