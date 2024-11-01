package com.dmh.authservice.exception;

public class AuthenticationProcessException extends RuntimeException {
  public AuthenticationProcessException(String message) {
    super(message);
  }
}
