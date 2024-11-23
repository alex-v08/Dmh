package com.dmh.cardservice.entity.dto;


import com.dmh.cardservice.entity.Card;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CREDIT")
@Data
@EqualsAndHashCode(callSuper = true)
public class CreditCard extends Card {
    @Column(precision = 10, scale = 2)
    private BigDecimal creditLimit;
}

