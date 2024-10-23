//package com.dmh.accountservice.exception;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import javax.security.auth.login.AccountNotFoundException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(AccountNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
//        log.error("Account not found: {}", ex.getMessage());
//        return new ResponseEntity<> (new ErrorResponse (ex.getMessage()), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(InvalidOperationException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException ex) {
//        log.error("Invalid operation: {}", ex.getMessage());
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//}
