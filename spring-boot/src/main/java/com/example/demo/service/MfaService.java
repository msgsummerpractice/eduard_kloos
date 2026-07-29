package com.example.demo.service;

public interface MfaService {

    String generateCode(String email);

    boolean verifyCode(String email, String code);

}