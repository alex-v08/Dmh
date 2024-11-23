package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.entity.Card;

import java.math.BigDecimal;

public class DebitCard extends Card {

    private String iban;

    public DebitCard() {
    }

    public DebitCard(String iban) {
        this.iban = iban;
    }

    public DebitCard(String iban, String number, String cvv, String expiryDate) {
        super(number, cvv, expiryDate);
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public String toString() {
        return "DebitCard{" +
                "iban='" + iban + '\'' +
                "} " + super.toString();
    }

    public BigDecimal getBalance () {
        return new BigDecimal(1000);
    }
}
