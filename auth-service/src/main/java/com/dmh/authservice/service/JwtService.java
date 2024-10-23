package com.dmh.authservice.service;

import com.dmh.authservice.model.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDTO user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("email", user.getEmail());

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating token for user: {}", user.getEmail(), e);
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public boolean validateToken(String token, UserDTO user) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }

            String jwt = token.substring(7);
            String username = extractUsername(jwt);

            if (username == null || !username.equals(user.getEmail())) {
                return false;
            }

            Claims claims = extractAllClaims(jwt);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("JWT token validation error: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }

    public Map<String, Object> extractAllCustomClaims(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> customClaims = new HashMap<>(claims);
        // Remove standard claims
        customClaims.remove("sub");
        customClaims.remove("iat");
        customClaims.remove("exp");
        return customClaims;
    }

    public boolean isTokenValid (String token, UserDTO user) {

        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        String jwt = token.substring(7);
        String username = extractUsername(jwt);

        if (username == null || !username.equals(user.getEmail())) {
            return false;
        }

        Claims claims = extractAllClaims(jwt);
        return !claims.getExpiration().before(new Date());
    }
}