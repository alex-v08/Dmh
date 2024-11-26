package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.entity.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("DEBIT")
@Data
@EqualsAndHashCode(callSuper = true)
public class DebitCard extends Card {

    @NotNull(message = "Balance cannot be null")
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "daily_limit", precision = 10, scale = 2)
    private BigDecimal dailyLimit;
}