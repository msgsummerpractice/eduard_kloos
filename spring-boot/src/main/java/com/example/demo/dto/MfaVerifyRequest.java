package com.example.demo.dto;

import lombok.Data;

@Data
public class MfaVerifyRequest {

    private String email;
    private String code;

}