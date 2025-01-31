package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.enums.CardBrand;
import com.dmh.cardservice.enums.CardType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardCreationDTO {


    private String expiration;
    private String number;
    private String holderName;
    private String cvc;
    private Long userId;
    private Long accountId;
    private CardType cardType;
    private CardBrand brand;
    private BigDecimal creditLimit;// Solo para CreditCard
    private BigDecimal balance;// Solo para DebitCard

}
