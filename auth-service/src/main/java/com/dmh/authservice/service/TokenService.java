package com.dmh.authservice.service;


import com.dmh.authservice.model.Token;
import com.dmh.authservice.model.TokenType;
import com.dmh.authservice.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    public Token saveToken(String jwtToken, Long userId, LocalDateTime expiresAt) {
        Token token = Token.builder()
                .token(jwtToken)
                .userId(userId)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .expiresAt(expiresAt)
                .build();

        return tokenRepository.save(token);
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(userId);
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }
}