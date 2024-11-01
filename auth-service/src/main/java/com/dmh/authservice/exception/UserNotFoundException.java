package com.dmh.authservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super (message);
    }

    public UserNotFoundException (String message, Exception e) {

    }
}
