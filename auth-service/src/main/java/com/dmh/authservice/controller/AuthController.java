package com.dmh.authservice.controller;

import com.dmh.authservice.model.AuthenticationRequest;
import com.dmh.authservice.model.AuthenticationResponse;
import com.dmh.authservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management endpoints")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user with email and password, returns a JWT token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        log.debug("Authentication request received for user: {}", request.getEmail());
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Logout user",
            description = "Invalidates the provided JWT token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully logged out"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid token"
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String token
    ) {
        log.debug("Logout request received");
        authenticationService.logout(token);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Refresh token",
            description = "Generates a new JWT token using a valid existing token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Token successfully refreshed"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestHeader("Authorization") String token
    ) {
        log.debug("Token refresh request received");
        AuthenticationResponse response = authenticationService.refreshToken(token);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Validate token",
            description = "Checks if a JWT token is valid"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Token validation result"
    )
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(
            @RequestHeader("Authorization") String token
    ) {
        log.debug("Token validation request received");
        boolean isValid = authenticationService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Error processing request", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
    }
}