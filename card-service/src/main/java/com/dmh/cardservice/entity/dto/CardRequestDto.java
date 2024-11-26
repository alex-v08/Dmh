package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.enums.CardType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CardRequestDto {
    @NotNull(message = "Card number is required")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String number;

    @NotNull(message = "Expiration date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{4}$", message = "Invalid expiration date format (MM/YYYY)")
    private String expirationDate;

    @NotNull(message = "Holder name is required")
    private String holderName;

    @NotNull(message = "CVC is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVC must be 3 or 4 digits")
    private String cvc;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private BigDecimal creditLimit; // Para tarjetas de crédito
    private BigDecimal balance;    // Para tarjetas de débito
}
