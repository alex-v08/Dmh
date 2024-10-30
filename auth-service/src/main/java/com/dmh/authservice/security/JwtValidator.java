package com.dmh.authservice.security;


import com.dmh.authservice.model.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.dmh.authservice.service.AuthenticationService;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtValidator {
    @Value("${jwt.secret}")
    private String secretKey;

    private final JwtValidator jwtValidator;

    public JwtValidator (JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }


    @Autowired
    public AuthenticationService(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }


    public boolean isTokenValid(String token, UserDTO user) {
        return jwtValidator.validateTokenForUser(token, user);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Método central para validación de tokens
     */
    public TokenValidationResult validateToken(String token) {
        try {
            if (token == null) {
                return TokenValidationResult.invalid("Token is null");
            }

            // Limpieza del token
            String cleanToken = cleanToken(token);

            // Validación de firma y parsing
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(cleanToken)
                    .getBody();

            // Validación de expiración
            if (claims.getExpiration().before(new Date ())) {
                return TokenValidationResult.invalid("Token has expired");
            }

            return TokenValidationResult.valid(claims);

        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return TokenValidationResult.invalid("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return TokenValidationResult.invalid("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            return TokenValidationResult.invalid("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            return TokenValidationResult.invalid("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return TokenValidationResult.invalid("JWT claims string is empty");
        }
    }

    /**
     * Validación específica para un usuario
     */
    public boolean validateTokenForUser(String token, UserDTO user) {
        TokenValidationResult validationResult = validateToken(token);

        if (!validationResult.isValid()) {
            return false;
        }

        String username = validationResult.getClaims().getSubject();
        return username != null && username.equals(user.getEmail());
    }

    /**
     * Limpieza del token (remover "Bearer " si existe)
     */
    private String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}


