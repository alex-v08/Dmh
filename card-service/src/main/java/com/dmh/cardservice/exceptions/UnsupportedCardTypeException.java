package com.dmh.cardservice.exceptions;

public class UnsupportedCardTypeException extends RuntimeException {

    // Constructor que acepta un mensaje
    public UnsupportedCardTypeException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public UnsupportedCardTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}