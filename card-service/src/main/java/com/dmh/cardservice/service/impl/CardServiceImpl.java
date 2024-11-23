package com.dmh.cardservice.service.impl;


import com.dmh.cardservice.entity.dto.AccountDto;
import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.Card;
import com.dmh.cardservice.entity.dto.CardRequestDto;
import com.dmh.cardservice.exceptions.CardNotFoundException;
import com.dmh.cardservice.mapper.ICardMapper;
import com.dmh.cardservice.repository.ICardRepository;

import com.dmh.cardservice.repository.client.AccountServiceClient;
import com.dmh.cardservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final ICardRepository cardRepository;
    private final ICardMapper ICardMapper;
    private final AccountServiceClient accountServiceClient;

    @Override
    public CardDto createCard (Long accountId, CardRequestDto cardRequestDto) {

        AccountDto account = accountServiceClient.getAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("La cuenta con ID " + accountId + " no existe.");
        }


        if (cardRepository.findByNumberId(cardRequestDto.getNumberId()).isPresent()) {
            throw new IllegalArgumentException("El número de identificación ya está asociado a otra tarjeta.");
        }


        Card card = ICardMapper.toCard(cardRequestDto);
        card.setAccountId(accountId);


        Card savedCard = cardRepository.save(card);


        return ICardMapper.toCardDto(savedCard);
    }

    @Override
    public List<CardDto> getCardsByAccountId (Long accountId) {
        AccountDto account = accountServiceClient.getAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("La cuenta con ID " + accountId + " no existe.");
        }

        List<Card> cards = cardRepository.findByAccountId(accountId);
        return cards.stream()
                .map(ICardMapper::toCardDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardDto getCardById (Long accountId, Long cardId) {
        AccountDto account = accountServiceClient.getAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("La cuenta con ID " + accountId + " no existe.");
        }

        Card card = cardRepository.findByAccountIdAndId(accountId, cardId)
                .orElseThrow(() -> new CardNotFoundException("Tarjeta no encontrada con ID: " + cardId + " para la cuenta: " + accountId));
        return ICardMapper.toCardDto(card);
    }

    @Override
    public void deleteCard (Long accountId, Long cardId) {
        AccountDto account = accountServiceClient.getAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("La cuenta con ID " + accountId + " no existe.");
        }

        Card card = cardRepository.findByAccountIdAndId(accountId, cardId)
                .orElseThrow(() -> new CardNotFoundException ("Tarjeta no encontrada con ID: " + cardId + " para la cuenta: " + accountId));
        cardRepository.delete(card);
    }
}
