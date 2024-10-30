package com.dmh.authservice.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException (String message) {
        super (message);
    }
}
