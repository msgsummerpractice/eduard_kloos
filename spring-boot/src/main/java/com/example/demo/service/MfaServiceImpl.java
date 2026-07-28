package com.example.demo.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class MfaServiceImpl implements MfaService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    private final Random random = new Random();

    @Override
    public String generateCode(String email) {

        String code = String.valueOf(
                100000 + random.nextInt(900000));

        otpStorage.put(email, code);

        System.out.println("Generated MFA code for " + email + ": " + code);

        return code;
    }

    @Override
    public boolean verifyCode(String email, String code) {

        String storedCode = otpStorage.get(email);

        if (storedCode == null) {
            return false;
        }

        boolean valid = storedCode.equals(code);

        if (valid) {
            otpStorage.remove(email);
        }

        return valid;
    }

}