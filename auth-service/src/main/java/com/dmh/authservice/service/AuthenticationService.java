package com.dmh.authservice.service;

import com.dmh.authservice.client.UserServiceClient;
import com.dmh.authservice.config.CustomUserDetails;
import com.dmh.authservice.config.JwtTokenProvider;
import com.dmh.authservice.exception.*;
import com.dmh.authservice.model.*;

import com.dmh.authservice.repository.TokenRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;  // Solo una instancia
    private final JwtTokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;  // Usar tu implementación personalizada de TokenService

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            log.debug("Intentando autenticar usuario con email: {}", request.getEmail());

            // Buscar usuario primero
            UserDTO user = findUserByEmail(request.getEmail());
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado");
            }

            // Autenticar credenciales
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Obtener detalles del usuario autenticado
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Revocar tokens existentes
            tokenService.revokeAllUserTokens(user.getId());

            // Generar nuevo token
            String jwt = tokenProvider.generateToken(
                    user.getEmail(),
                    user.getId(),
                    tokenProvider.getJwtExpiration()
            );

            // Guardar el nuevo token
            tokenService.saveToken(jwt, user.getId(), LocalDateTime.now().plusDays(1));

            log.info("Usuario autenticado exitosamente: {}", user.getEmail());

            // Retornar respuesta
            return AuthenticationResponse.builder()
                    .token(jwt)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Credenciales inválidas para el usuario: {}", request.getEmail());
            throw new InvalidCredentialsException("Credenciales inválidas");
        } catch (UserNotFoundException e) {
            log.error("Usuario no encontrado: {}", request.getEmail());
            throw e;
        } catch (Exception e) {
            log.error("Error en el proceso de autenticación", e);
            throw new AuthenticationProcessException("Error en el proceso de autenticación");
        }
    }

    @Transactional
    public void logout(String authHeader) {
        try {
            String jwt = extractTokenFromHeader(authHeader);
            log.debug("Procesando logout para token: {}", jwt);

            Token token = tokenRepository.findByToken(jwt)
                    .orElseThrow(() -> new TokenNotFoundException("Token no encontrado"));

            token.setRevoked(true);
            token.setExpired(true);
            token.setExpiresAt(LocalDateTime.now());

            tokenRepository.save(token);
            log.info("Logout exitoso");
        } catch (TokenNotFoundException e) {
            log.error("Token no encontrado durante logout");
            throw e;
        } catch (Exception e) {
            log.error("Error durante el proceso de logout", e);
            throw new LogoutProcessException("Error durante el proceso de logout");
        }
    }

    public boolean validateToken(String authHeader) {
        try {
            String jwt = extractTokenFromHeader(authHeader);
            log.debug("Validando token: {}", jwt);

            Token token = tokenRepository.findByToken(jwt)
                    .orElseThrow(() -> new TokenNotFoundException("Token no encontrado"));

            if (token.isExpired() || token.isRevoked()) {
                log.debug("Token expirado o revocado");
                return false;
            }

            boolean isValid = tokenProvider.validateToken(jwt);
            log.debug("Token validado: {}", isValid);
            return isValid;
        } catch (Exception e) {
            log.error("Error en la validación del token", e);
            return false;
        }
    }

    public UserDTO findUserByEmail(String email) {
        try {
            log.debug("Buscando usuario por email: {}", email);
            ResponseEntity<UserDTO> response = userServiceClient.findByEmail(email);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new UserNotFoundException("Usuario no encontrado con email: " + email);
            }

            return response.getBody();
        } catch (FeignException e) {
            log.error("Error al buscar usuario por email: {}", email, e);
            throw new UserNotFoundException("Error al buscar usuario: " + e.getMessage());
        }
    }

    public UserDTO findUserById(Long id) {
        try {
            log.debug("Buscando usuario por ID: {}", id);
            ResponseEntity<UserDTO> response = userServiceClient.findById(id);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
            }

            return response.getBody();
        } catch (FeignException e) {
            log.error("Error al buscar usuario por ID: {}", id, e);
            throw new UserNotFoundException("Error al buscar usuario: " + e.getMessage());
        }
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }
}