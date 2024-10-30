package com.dmh.authservice.service;

import com.dmh.authservice.client.UserServiceClient;
import com.dmh.authservice.exception.InvalidCredentialsException;
import com.dmh.authservice.exception.InvalidTokenException;
import com.dmh.authservice.model.AuthenticationRequest;
import com.dmh.authservice.model.AuthenticationResponse;
import com.dmh.authservice.model.dto.UserDTO;
import com.dmh.authservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Autenticar credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Buscar usuario
            UserDTO user = userServiceClient.findByEmail(request.getEmail());
            if (user == null) {
                throw new InvalidCredentialsException("Usuario no encontrado");
            }

            // Generar token JWT
            String token = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(token)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .build();

        } catch (Exception e) {
            log.error("Authentication failed for user: {}", request.getEmail(), e);
            throw new InvalidCredentialsException("Error en la autenticación");
        }
    }

    @Transactional
    public void logout(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException("Token inválido");
        }

        String jwt = token.substring(7);
        jwtService.revokeToken(jwt);
    }

    @Transactional
    public AuthenticationResponse refreshToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException("Token inválido");
        }

        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);

        UserDTO user = userServiceClient.findByEmail(email);
        if (user == null) {
            throw new InvalidCredentialsException("Usuario no encontrado");
        }

        String newToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(newToken)
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Scheduled(cron = "0 0 * * * *") // Cada hora
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

    public boolean validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        String jwt = token.substring(7);
        return jwtService.validateToken(jwt);
    }

    // Método helper para extraer el token JWT del header Authorization
    private String extractJwtFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new InvalidTokenException("Token inválido o formato incorrecto");
    }
}