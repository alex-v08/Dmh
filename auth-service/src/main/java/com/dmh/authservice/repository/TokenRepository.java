package com.dmh.authservice.repository;

import com.dmh.authservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query("""
        select t from Token t 
        where t.userId = :userId and 
        (t.expired = false or t.revoked = false)
    """)
    List<Token> findAllValidTokensByUserId(Long userId);

    @Query("""
        select t from Token t 
        where t.expired = false and 
        t.revoked = false and 
        t.expiresAt > CURRENT_TIMESTAMP
    """)
    List<Token> findAllValidTokens();

    void deleteByUserId(Long userId);

    void deleteExpiredTokens (LocalDateTime now);

}