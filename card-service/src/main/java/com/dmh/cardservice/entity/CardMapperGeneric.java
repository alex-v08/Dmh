package com.dmh.cardservice.entity;

import com.DigitalMoneyHouse.msvc_cards.models.dto.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_cards.models.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.models.entity.CreditCard;
import com.DigitalMoneyHouse.msvc_cards.models.entity.DebitCard;
import com.DigitalMoneyHouse.msvc_cards.models.enums.CardType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class CardMapperGeneric {

    public  Card toEntity(CardCreationDTO cardDTO) {
        if (cardDTO.getCardType() == CardType.CREDIT) {
            return toCreditCard(cardDTO);
        } else if (cardDTO.getCardType() == CardType.DEBIT) {
            return toDebitCard(cardDTO);
        } else {
            throw new IllegalArgumentException("Tipo de tarjeta no soportado.");
        }
    }

    public  CreditCard toCreditCard(CardCreationDTO cardDTO) {
        CreditCard creditCard = new CreditCard();
        creditCard.setExpiration(cardDTO.getExpiration());
        creditCard.setNumber(cardDTO.getNumber());
        creditCard.setHolderName(cardDTO.getHolderName());
        creditCard.setCvc(cardDTO.getCvc());
        creditCard.setUserId(cardDTO.getUserId());
        creditCard.setAccountId(cardDTO.getAccountId());
        creditCard.setBrand(cardDTO.getBrand());
        creditCard.setCardType(cardDTO.getCardType());
        // Si creditLimit es null, asignar 0.00
        creditCard.setCreditLimit(cardDTO.getCreditLimit() != null ? cardDTO.getCreditLimit() : BigDecimal.ZERO);

        return creditCard;
    }

    public  DebitCard toDebitCard(CardCreationDTO cardDTO) {
        DebitCard debitCard = new DebitCard();
        debitCard.setExpiration(cardDTO.getExpiration());
        debitCard.setNumber(cardDTO.getNumber());
        debitCard.setHolderName(cardDTO.getHolderName());
        debitCard.setCvc(cardDTO.getCvc());
        debitCard.setUserId(cardDTO.getUserId());
        debitCard.setAccountId(cardDTO.getAccountId());
        debitCard.setBrand(cardDTO.getBrand());
        debitCard.setCardType(cardDTO.getCardType());
        // Si Balance es null, asignar 0.00
        debitCard.setBalance(cardDTO.getBalance() != null? cardDTO.getBalance() : BigDecimal.ZERO);

        return debitCard;
    }

    public CardRequestDTO toCardRequestDTO(Card card) {
        CardRequestDTO cardRequestDTO = new CardRequestDTO();
        cardRequestDTO.setId(card.getId());
        cardRequestDTO.setExpiration(card.getExpiration());
        cardRequestDTO.setNumber(card.getNumber());
        cardRequestDTO.setHolderName(card.getHolderName());
        cardRequestDTO.setCvc(card.getCvc());
        cardRequestDTO.setCardType(card.getCardType());
        cardRequestDTO.setBrand(card.getBrand());

        // Mapear según el tipo de tarjeta
        if (card instanceof CreditCard) {
            cardRequestDTO.setCreditLimit(((CreditCard) card).getCreditLimit());
        } else if (card instanceof DebitCard) {
            cardRequestDTO.setBalance(((DebitCard) card).getBalance());
        }

        return cardRequestDTO;
    }
}