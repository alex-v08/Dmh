package com.dmh.cardservice.exceptions;

import com.DigitalMoneyHouse.msvc_cards.models.enums.CardType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja excepciones cuando la tarjeta ya existe.
    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCardAlreadyExistsException(CardAlreadyExistsException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("La tarjeta ya está asociada a otra cuenta", "CARD_ALREADY_EXISTS");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Maneja excepciones cuando el tipo de tarjeta no está soportado
    @ExceptionHandler(UnsupportedCardTypeException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnsupportedCardTypeException(UnsupportedCardTypeException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Tipo de tarjeta no soportado", "UNSUPPORTED_CARD_TYPE");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // Maneja excepciones específicas de deserialización JSON.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
// Mensaje de error generalizado para documentación
        String errorMessage = "Los datos proporcionados no son válidos. Asegúrese de que la solicitud siga la siguiente estructura:\n\n" +
                "{" +
                "\"expiration\": \"string\"," +
                "\"number\": \"string\"," +
                "\"holderName\": \"string\"," +
                "\"cvc\": \"string\"," +
                "\"cardType\": \"CREDIT o DEBIT\"," +
                "\"brand\": \"VISA, MASTERCARD, AMEX, UNKNOWN\"," +
                "\"creditLimit\": \"decimal (solo para CREDIT)\"," +
                "\"balance\": \"decimal (solo para DEBIT)\"" +
                "}\n\n" +
                "Revise los datos enviados y vuelva a intentarlo.";





        // Mensaje genérico para otros errores de deserialización
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorMessage, "INVALID_DATA_FORMAT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /*
    // Maneja excepciones específicas de deserialización JSON.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // Extraer el mensaje específico de la excepción
        Throwable cause = ex.getCause();

        if (cause instanceof JsonMappingException jsonMappingException) {
            // Verificar si el error está relacionado con un tipo de tarjeta inválido
            if (jsonMappingException.getCause() instanceof InvalidFormatException invalidFormatException) {
                if (invalidFormatException.getTargetType() == CardType.class) {
                    String invalidValue = invalidFormatException.getValue().toString();
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                            "El valor \"" + invalidValue + "\" para cardType no es válido.",
                            "INVALID_CARD_TYPE"
                    );
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }
        }

        // Mensaje genérico para otros errores de deserialización
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Error en el formato del dato", "INVALID_DATA_FORMAT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    */


    // Maneja excepciones generales no específicas.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Error no manejado: " + ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Error interno del servidor", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
