package com.dmh.cardservice.repository;

import com.dmh.cardservice.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

    List<Card> findByAccountId(Long accountId);
    Optional<Card> findByAccountIdAndId(Long accountId, Long cardId);
    Optional<Card> findByNumber(String number);  // Cambiado de findByNumberId a findByNumber


}
