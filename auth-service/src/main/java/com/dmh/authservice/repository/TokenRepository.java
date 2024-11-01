package com.dmh.authservice.repository;

import com.dmh.authservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    List<Token> findAllValidTokensByUserId(@Param("userId") Long userId);

    @Query("""
        select t from Token t 
        where t.expired = false and 
        t.revoked = false and 
        t.expiresAt > CURRENT_TIMESTAMP
    """)
    List<Token> findAllValidTokens();

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
        delete from Token t 
        where t.expiresAt < :now or 
        (t.expired = true and t.revoked = true)
    """)
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Query("""
        select t from Token t 
        where t.expired = true or t.revoked = true
    """)
    List<Token> findAllByExpiredTrueOrRevokedTrue();
}