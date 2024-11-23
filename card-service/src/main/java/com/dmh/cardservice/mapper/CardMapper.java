package com.dmh.cardservice.mapper;


import com.dmh.cardservice.entity.Card;
import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.dto.CardRequestDto;
import com.dmh.cardservice.entity.dto.CreditCard;
import com.dmh.cardservice.entity.dto.DebitCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;



import java.util.List;

import static com.dmh.cardservice.enums.CardType.CREDIT;
import static com.dmh.cardservice.enums.CardType.DEBIT;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "cod", target = "cod")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @Mapping(source = "firstLastName", target = "firstLastName")
    @Mapping(source = "numberId", target = "numberId")
    @Mapping(source = "creditLimit", target = "creditLimit")
    CreditCard toCreditCard(CardRequestDto cardRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "cod", target = "cod")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @Mapping(source = "firstLastName", target = "firstLastName")
    @Mapping(source = "numberId", target = "numberId")
    @Mapping(source = "balance", target = "balance")
    DebitCard toDebitCard(CardRequestDto cardRequestDto);

    @Mapping(target = "cardType", expression = "java(determineCardType(card))")
    @Mapping(target = "creditLimit", expression = "java(getCreditLimit(card))")
    @Mapping(target = "balance", expression = "java(getBalance(card))")
    CardDto toCardDto(Card card);

    List<CardDto> toCardDtoList(List<Card> cards);

    @Mapping(target = "id", ignore = true)
    void updateCardFromDto(CardDto cardDto, @MappingTarget Card card);

    default Card createCard(CardRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        switch (requestDto.getCardType()) {
            case CREDIT:
                return toCreditCard(requestDto);
            case DEBIT:
                return toDebitCard(requestDto);
            default:
                throw new IllegalArgumentException("Tipo de tarjeta no soportado: " + requestDto.getCardType());
        }
    }

    default com.dmh.cardservice.enums.CardType determineCardType(Card card) {
        if (card instanceof CreditCard) {
            return CREDIT;
        } else if (card instanceof DebitCard) {
            return DEBIT;
        }
        return null;
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

    Card toCard (CardRequestDto cardRequestDto);

}