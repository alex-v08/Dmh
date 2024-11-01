package com.dmh.authservice.controller;

import com.dmh.authservice.model.AuthenticationRequest;
import com.dmh.authservice.model.AuthenticationResponse;
import com.dmh.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok (authenticationService.authenticate (request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout (
            @RequestHeader("Authorization") String token) {
        authenticationService.logout (token);
        return ResponseEntity.ok ().build ();
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken (
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok (authenticationService.validateToken (token));
    }
}