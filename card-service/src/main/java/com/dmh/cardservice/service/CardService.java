package com.dmh.cardservice.service;

import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.dto.CardRequestDto;

import java.util.List;

public interface CardService {
    CardDto createCard(Long accountId, CardRequestDto cardRequestDto);
    List<CardDto> getCardsByAccountId(Long accountId);
    CardDto getCardById(Long accountId, Long cardId);
    void deleteCard(Long accountId, Long cardId);
}
