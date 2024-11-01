package com.dmh.authservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException (String message) {
        super (message);
    }
}
