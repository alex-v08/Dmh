package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.enums.CardType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CardDto {
    private Long id;
    private Long accountId;
    private String number;
    private String expirationDate;
    private String holderName;
    private String cvc;
    private CardType cardType;
    private BigDecimal creditLimit;
    private BigDecimal balance;
}