package com.dmh.authservice.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException (String message) {
        super (message);
    }
}
