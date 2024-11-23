package com.dmh.cardservice.entity;


import com.dmh.cardservice.enums.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Card number cannot be null")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    @Column(nullable = false, unique = true)
    private String number;

    @NotNull(message = "CVV cannot be null")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits")
    @Column(nullable = false)
    private String cvv;

    @NotNull(message = "Expiry date cannot be null")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{4}$",
            message = "Expiry date must be in format MM/YYYY")
    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @NotNull(message = "Account ID cannot be null")
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", insertable = false, updatable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardStatus status = CardStatus.ACTIVE;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    // Constructor con los campos básicos
    protected Card(String number, String cvv, String expiryDate) {
        this.number = number;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.status = CardStatus.ACTIVE;
    }

    /**
     * Verifica si la tarjeta está activa
     * @return true si la tarjeta está activa
     */
    public boolean isActive() {
        return CardStatus.ACTIVE.equals(this.status);
    }

    /**
     * Verifica si la tarjeta está expirada
     * @return true si la tarjeta está expirada
     */
    public boolean isExpired() {
        if (expiryDate == null) return true;

        String[] parts = expiryDate.split("/");
        if (parts.length != 2) return true;

        try {
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime cardExpiry = LocalDateTime.of(year, month, 1, 0, 0)
                    .plusMonths(1).minusSeconds(1);

            return cardExpiry.isBefore(now);
        } catch (NumberFormatException e) {
            return true;
        }
    }
}