package com.dmh.cardservice.entity.dto;

import com.dmh.cardservice.entity.Card;
import com.dmh.cardservice.enums.CardType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("DEBIT")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DebitCard extends Card {

    @NotNull(message = "IBAN cannot be null")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$",
            message = "IBAN must follow the international format")
    @Column(name = "iban", unique = true)
    private String iban;

    @NotNull(message = "Balance cannot be null")
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(name = "daily_withdrawal_limit", precision = 10, scale = 2)
    private BigDecimal dailyWithdrawalLimit;

    @Column(name = "monthly_withdrawal_limit", precision = 10, scale = 2)
    private BigDecimal monthlyWithdrawalLimit;

    public DebitCard(String iban, String number, String cvv, String expiryDate) {
        super(number, cvv, expiryDate);
        this.iban = iban;
        this.setCardType(CardType.DEBIT);
        this.balance = BigDecimal.ZERO;
        // Establecer límites por defecto
        this.dailyWithdrawalLimit = new BigDecimal("1000.00");
        this.monthlyWithdrawalLimit = new BigDecimal("5000.00");
    }

    /**
     * Actualiza el balance de la tarjeta
     * @param amount monto a actualizar (positivo para depósitos, negativo para retiros)
     * @throws IllegalArgumentException si el retiro excede el balance disponible
     */
    public void updateBalance(BigDecimal amount) {
        BigDecimal newBalance = this.balance.add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance = newBalance;
    }

    /**
     * Verifica si un retiro está dentro de los límites permitidos
     * @param amount monto a retirar
     * @return true si el retiro está permitido
     */
    public boolean isWithdrawalAllowed(BigDecimal amount) {
        return amount.compareTo(dailyWithdrawalLimit) <= 0 &&
                amount.compareTo(monthlyWithdrawalLimit) <= 0 &&
                amount.compareTo(balance) <= 0;
    }

    /**
     * Establece los límites de retiro
     * @param dailyLimit límite diario
     * @param monthlyLimit límite mensual
     * @throws IllegalArgumentException si los límites son inválidos
     */
    public void setWithdrawalLimits(BigDecimal dailyLimit, BigDecimal monthlyLimit) {
        if (dailyLimit.compareTo(BigDecimal.ZERO) <= 0 ||
                monthlyLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal limits must be positive");
        }
        if (dailyLimit.compareTo(monthlyLimit) > 0) {
            throw new IllegalArgumentException("Daily limit cannot exceed monthly limit");
        }
        this.dailyWithdrawalLimit = dailyLimit;
        this.monthlyWithdrawalLimit = monthlyLimit;
    }
}