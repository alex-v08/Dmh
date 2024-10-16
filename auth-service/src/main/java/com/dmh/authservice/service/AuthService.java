package com.dmh.authservice.service;

import com.dmh.authservice.exception.InvalidCredentialsException;
import com.dmh.authservice.exception.TokenExpiredException;
import com.dmh.authservice.model.LoginRequest;
import com.dmh.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    // En una implementación real, esto debería ser una base de datos o un servicio de caché
    private Set<String> invalidatedTokens = new HashSet<>();

    public String login(LoginRequest loginRequest) {
        // En una implementación real, aquí verificarías las credenciales contra un servicio de usuarios
        if ("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            return jwtUtil.generateToken(loginRequest.getUsername());
        }
        throw new InvalidCredentialsException("Invalid username or password");
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        invalidatedTokens.add(token);
    }

    public boolean validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (invalidatedTokens.contains(token)) {
            throw new TokenExpiredException("Token has been invalidated");
        }

        return jwtUtil.validateToken(token);
    }

    public String refreshToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (invalidatedTokens.contains(token)) {
            throw new TokenExpiredException("Token has been invalidated");
        }

        String username = jwtUtil.getUsernameFromToken(token);
        return jwtUtil.generateToken(username);
    }
}