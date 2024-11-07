package com.dmh.authservice.service;

import com.dmh.authservice.client.UserServiceClient;
import com.dmh.authservice.config.CustomUserDetails;
import com.dmh.authservice.config.JwtTokenProvider;
import com.dmh.authservice.exception.*;
import com.dmh.authservice.model.*;
import com.dmh.authservice.repository.TokenRepository;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final JwtTokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    @Operation(
            summary = "Authenticate user",
            description = "Search a user by email and password. Return a new token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful authentication",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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
                    new UsernamePasswordAuthenticationToken (
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            tokenService.revokeAllUserTokens(user.getId());

            String jwt = tokenProvider.generateToken(
                    user.getEmail(),
                    user.getId(),
                    tokenProvider.getJwtExpiration()
            );

            tokenService.saveToken(jwt, user.getId(), LocalDateTime.now().plusDays(1));
            log.info("Usuario autenticado exitosamente: {}", user.getEmail());

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

    @Operation(
            summary = "Logout current session",
            description = "Ends current session and invalidates the token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Successfully logged out"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Token not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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

    @Operation(
            summary = "Validate token",
            description = "Validates if a token is valid and not expired/revoked"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token validation result",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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

    @Operation(
            summary = "Find user by email",
            description = "Retrieves user information by email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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

    @Operation(
            summary = "Find user by ID",
            description = "Retrieves user information by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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