package com.dmh.authservice.security;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class TokenValidationResult {
    boolean valid;
    String errorMessage;
    Claims claims;

    public static TokenValidationResult valid(Claims claims) {
        return TokenValidationResult.builder()
                .valid(true)
                .claims(claims)
                .build();
    }

    public static TokenValidationResult invalid(String errorMessage) {
        return TokenValidationResult.builder()
                .valid(false)
                .errorMessage(errorMessage)
                .build();
    }
}