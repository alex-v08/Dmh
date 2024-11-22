package com.dmh.cardservice.exceptions;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String message;
    private String errorCode;

    public ErrorResponseDTO(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters y Setters
}
