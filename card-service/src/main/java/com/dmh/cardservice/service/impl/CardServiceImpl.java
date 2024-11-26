package com.dmh.cardservice.service.impl;

import com.dmh.cardservice.entity.dto.*;
import com.dmh.cardservice.entity.Card;

import com.dmh.cardservice.enums.CardType;
import com.dmh.cardservice.exceptions.CardAlreadyExistsException;
import com.dmh.cardservice.exceptions.CardNotFoundException;

import com.dmh.cardservice.exceptions.InvalidOperationException;
import com.dmh.cardservice.mapper.ICardMapper;
import com.dmh.cardservice.repository.ICardRepository;
import com.dmh.cardservice.repository.client.AccountServiceClient;
import com.dmh.cardservice.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final ICardRepository cardRepository;
    private final ICardMapper cardMapper;
    private final AccountServiceClient accountServiceClient;

    @Override
    @Transactional
    public CardDto createCard(Long accountId, CardRequestDto cardRequestDto) {
        log.debug("Creating card for account ID: {} with request: {}", accountId, cardRequestDto);

        // Validar cuenta
        AccountDto account = validateAccount(accountId);

        // Validar número de tarjeta único
        validateUniqueCardNumber(cardRequestDto.getNumber());

        // Crear tarjeta según el tipo
        Card card = createCardByType(cardRequestDto);
        card.setAccountId(accountId);

        // Validaciones específicas por tipo de tarjeta
        validateCardSpecificRequirements(card, cardRequestDto);

        // Guardar y mapear respuesta
        Card savedCard = cardRepository.save(card);
        CardDto responseDto = cardMapper.cardToCardDto(savedCard);

        log.info("Card created successfully for account ID: {}", accountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> getCardsByAccountId(Long accountId) {
        log.debug("Fetching cards for account ID: {}", accountId);

        validateAccount(accountId);

        List<Card> cards = cardRepository.findByAccountId(accountId);
        return cards.stream()
                .map(cardMapper::cardToCardDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto getCardById(Long accountId, Long cardId) {
        log.debug("Fetching card ID: {} for account ID: {}", cardId, accountId);

        validateAccount(accountId);
        Card card = findCardByAccountAndId(accountId, cardId);

        return cardMapper.cardToCardDto(card);
    }

    @Override
    @Transactional
    public void deleteCard(Long accountId, Long cardId) {
        log.debug("Deleting card ID: {} for account ID: {}", cardId, accountId);

        validateAccount(accountId);
        Card card = findCardByAccountAndId(accountId, cardId);

        cardRepository.delete(card);
        log.info("Card deleted successfully - ID: {} for account ID: {}", cardId, accountId);
    }

    // Helper Methods
    private AccountDto validateAccount(Long accountId) {
        AccountDto account = accountServiceClient.getAccountById(accountId);
        if (account == null) {
            throw new InvalidOperationException ("Account not found with ID: " + accountId);
        }
        return account;
    }

    private void validateUniqueCardNumber(String cardNumber) {
        if (cardRepository.findByNumber(cardNumber).isPresent()) {
            throw new CardAlreadyExistsException("Card with number " + cardNumber + " already exists");
        }
    }

    private Card createCardByType(CardRequestDto requestDto) {
        Card card;
        if (CardType.CREDIT.equals(requestDto.getCardType())) {
            CreditCard creditCard = new CreditCard ();
            creditCard.setCreditLimit(requestDto.getCreditLimit());
            creditCard.setCurrentBalance(BigDecimal.ZERO);
            creditCard.setAvailableCredit(requestDto.getCreditLimit());
            card = creditCard;
        } else if (CardType.DEBIT.equals(requestDto.getCardType())) {
            DebitCard debitCard = new DebitCard ();
            debitCard.setBalance(requestDto.getBalance() != null ? requestDto.getBalance() : BigDecimal.ZERO);
            debitCard.setDailyLimit(new BigDecimal("1000.00")); // Default daily limit
            card = debitCard;
        } else {
            throw new InvalidOperationException("Unsupported card type: " + requestDto.getCardType());
        }

        // Set common properties
        card.setNumber(requestDto.getNumber());
        card.setCvv(requestDto.getCvc());
        card.setExpiryDate(requestDto.getExpirationDate());
        return card;
    }

    private void validateCardSpecificRequirements(Card card, CardRequestDto requestDto) {
        if (card instanceof CreditCard) {
            if (requestDto.getCreditLimit() == null || requestDto.getCreditLimit().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidOperationException("Credit limit must be greater than zero for credit cards");
            }
        }
        // Add more specific validations as needed
    }

    private Card findCardByAccountAndId(Long accountId, Long cardId) {
        return cardRepository.findByAccountIdAndId(accountId, cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + cardId +
                        " for account: " + accountId));
    }
}