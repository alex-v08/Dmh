package com.dmh.authservice.service;


import com.dmh.authservice.exception.TokenGenerationException;
import com.dmh.authservice.model.Token;
import com.dmh.authservice.model.TokenType;
import com.dmh.authservice.model.dto.UserDTO;
import com.dmh.authservice.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public String generateToken(UserDTO user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("email", user.getEmail());

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();

            saveToken(token, user.getId());
            return token;
        } catch (Exception e) {
            log.error("Error generating token for user: {}", user.getEmail(), e);
            throw new TokenGenerationException ("Error generating JWT token");
        }
    }

    public boolean validateToken(String token) {
        if (!isTokenValid(token)) {
            return false;
        }

        String username = extractUsername(token);
        return username != null &&
                username.equals(user.getEmail()) &&
                !isTokenRevoked(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenRevoked(String token) {
        return tokenRepository.findByToken(token)
                .map(Token::isRevoked)
                .orElse(true);
    }

    @Transactional
    public void revokeToken(String token) {
        tokenRepository.findByToken(token)
                .ifPresent(storedToken -> {
                    storedToken.setRevoked(true);
                    storedToken.setExpired(true);
                    tokenRepository.save(storedToken);
                });
    }

    private void saveToken(String token, Long userId) {
        Token tokenEntity = Token.builder()
                .token(token)
                .userId(userId)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(jwtExpiration / 1000))
                .build();

        tokenRepository.save(tokenEntity);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
