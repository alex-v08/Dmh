package com.dmh.accountservice.entity.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private String accountNumber;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @PositiveOrZero(message = "Balance must be zero or positive")
    private BigDecimal balance;

    private String cvu;
    private String alias;


}
