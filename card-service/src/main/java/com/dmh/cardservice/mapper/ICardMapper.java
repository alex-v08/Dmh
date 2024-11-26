package com.dmh.cardservice.mapper;

import com.dmh.cardservice.entity.*;
import com.dmh.cardservice.entity.dto.*;
import com.dmh.cardservice.enums.CardType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ICardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "cvv", source = "cvc")
    @Mapping(target = "expiryDate", source = "expirationDate")
    default Card cardRequestDtoToCard(CardRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Card card;
        if (CardType.CREDIT.equals(requestDto.getCardType())) {
            CreditCard creditCard = new CreditCard();
            creditCard.setCreditLimit(requestDto.getCreditLimit());
            card = creditCard;
        } else {
            DebitCard debitCard = new DebitCard();
            debitCard.setBalance(requestDto.getBalance());
            card = debitCard;
        }

        card.setNumber(requestDto.getNumber());
        card.setCvv(requestDto.getCvc());
        card.setExpiryDate(requestDto.getExpirationDate());
        card.setAccountId(requestDto.getAccountId());

        return card;
    }

    @Mapping(target = "number", source = "number")
    @Mapping(target = "cvc", source = "cvv")
    @Mapping(target = "expirationDate", source = "expiryDate")
    @Mapping(target = "cardType", expression = "java(getCardType(card))")
    @Mapping(target = "creditLimit", expression = "java(getCreditLimit(card))")
    @Mapping(target = "balance", expression = "java(getBalance(card))")
    CardDto cardToCardDto(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromDto(CardDto cardDto, @MappingTarget Card card);

    default CardType getCardType(Card card) {
        if (card instanceof CreditCard) {
            return CardType.CREDIT;
        }
        return CardType.DEBIT;
    }

    default java.math.BigDecimal getCreditLimit(Card card) {
        if (card instanceof CreditCard) {
            return ((CreditCard) card).getCreditLimit();
        }
        return null;
    }

    default java.math.BigDecimal getBalance(Card card) {
        if (card instanceof DebitCard) {
            return ((DebitCard) card).getBalance();
        }
        return null;
    }
}