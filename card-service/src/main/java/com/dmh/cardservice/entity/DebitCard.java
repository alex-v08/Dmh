package com.dmh.cardservice.entity;

import com.DigitalMoneyHouse.msvc_cards.models.enums.CardBrand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
public class DebitCard extends Card {
    // Getters y Setters

    private BigDecimal balance; // Saldo disponible



   }