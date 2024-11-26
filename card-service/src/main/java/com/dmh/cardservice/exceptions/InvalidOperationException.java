package com.dmh.cardservice.exceptions;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException (String message) {
        super (message);
    }
}
