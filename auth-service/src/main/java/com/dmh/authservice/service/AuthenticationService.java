package com.dmh.authservice.service;

import com.dmh.authservice.client.UserServiceClient;
import com.dmh.authservice.exception.InvalidCredentialsException;
import com.dmh.authservice.exception.TokenExpiredException;
import com.dmh.authservice.model.AuthenticationRequest;
import com.dmh.authservice.model.AuthenticationResponse;
import com.dmh.authservice.model.Token;
import com.dmh.authservice.model.TokenType;
import com.dmh.authservice.model.dto.UserDTO;
import com.dmh.authservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Validar credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Obtener usuario del servicio de usuarios
            UserDTO user = userServiceClient.findByEmail(request.getEmail());
            if (user == null) {
                logger.error("Usuario no encontrado para el email: {}", request.getEmail());
                throw new InvalidCredentialsException("Usuario no encontrado");
            }

            // Generar token JWT y manejar expiración
            String jwtToken = jwtService.generateToken(user);
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtService.getJwtExpiration() / 1000);

            // Revocar tokens anteriores y guardar nuevo token
            handleTokenManagement(user.getId(), jwtToken, expiresAt);

            logger.info("Autenticación exitosa para el usuario: {}", user.getEmail());

            return createAuthenticationResponse(jwtToken, user);

        } catch (BadCredentialsException e) {
            logger.warn("Intento de autenticación fallido para el email: {}", request.getEmail());
            throw new InvalidCredentialsException("Credenciales inválidas");
        } catch (AuthenticationException e) {
            logger.error("Error durante la autenticación para el email: {}", request.getEmail(), e);
            throw new InvalidCredentialsException("Error en la autenticación");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void logout(String token) {
        String cleanToken = extractToken(token);
        var storedToken = tokenRepository.findByToken(cleanToken)
                .orElseThrow(() -> new TokenExpiredException("Token no encontrado"));

        revokeToken(storedToken);
        logger.info("Logout exitoso para el token: {}", cleanToken);
    }

    public boolean validateToken(String token) {
        String cleanToken = extractToken(token);
        try {
            return performTokenValidation(cleanToken);
        } catch (Exception e) {
            logger.error("Error validando token", e);
            throw new TokenExpiredException("Token inválido o expirado");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse refreshToken(String token) {
        String cleanToken = extractToken(token);
        var storedToken = tokenRepository.findByToken(cleanToken)
                .orElseThrow(() -> new TokenExpiredException("Token no encontrado"));

        if (storedToken.isExpired() || storedToken.isRevoked()) {
            throw new TokenExpiredException("Token expirado o revocado");
        }

        String email = jwtService.extractUsername(cleanToken);
        UserDTO user = userServiceClient.findByEmail(email);

        if (user == null) {
            throw new InvalidCredentialsException("Usuario no encontrado");
        }

        String newToken = jwtService.generateToken(user);
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtService.getJwtExpiration() / 1000);

        handleTokenManagement(user.getId(), newToken, expiresAt);

        logger.info("Token refrescado exitosamente para el usuario: {}", user.getEmail());

        return createAuthenticationResponse(newToken, user);
    }

    private void handleTokenManagement(Long userId, String jwtToken, LocalDateTime expiresAt) {
        revokeAllUserTokens(userId);
        saveUserToken(userId, jwtToken, expiresAt);
    }

    private AuthenticationResponse createAuthenticationResponse(String token, UserDTO user) {
        return AuthenticationResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    private String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    private boolean performTokenValidation(String token) {
        var tokenEntity = tokenRepository.findByToken(token)
                .orElse(null);

        if (tokenEntity == null || tokenEntity.isExpired() || tokenEntity.isRevoked()) {
            logger.warn("Token inválido o expirado: {}", token);
            return false;
        }

        String email = jwtService.extractUsername(token);
        if (email == null) {
            logger.warn("No se pudo extraer el email del token");
            return false;
        }

        UserDTO user = userServiceClient.findByEmail(email);
        if (user == null) {
            logger.warn("Usuario no encontrado para el email: {}", email);
            return false;
        }

        return jwtService.isTokenValid(token, user);
    }

    private void revokeToken(Token token) {
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }

    @Transactional(rollbackFor = Exception.class)
    void revokeAllUserTokens(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(userId);
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
        logger.debug("Tokens revocados para el usuario: {}", userId);
    }

    @Transactional(rollbackFor = Exception.class)
    void saveUserToken(Long userId, String jwtToken, LocalDateTime expiresAt) {
        var token = Token.builder()
                .userId(userId)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .expiresAt(expiresAt)
                .createdAt(LocalDateTime.now())
                .build();

        tokenRepository.save(token);
        logger.debug("Nuevo token guardado para el usuario: {}", userId);
    }
}