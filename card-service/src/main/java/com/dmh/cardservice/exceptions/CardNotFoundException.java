package com.dmh.cardservice.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException (String message) {
        super (message);
    }
}
