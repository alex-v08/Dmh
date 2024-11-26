package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.entity.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CREDIT")
@Data
@EqualsAndHashCode(callSuper = true)
public class CreditCard extends Card {

    @NotNull(message = "Credit limit cannot be null")
    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "current_balance", precision = 10, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "available_credit", precision = 10, scale = 2)
    private BigDecimal availableCredit;
}