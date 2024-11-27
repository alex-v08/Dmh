package com.dmh.gateway.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException (String message) {
        super (message);
    }
}
