package com.dmh.accountservice.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table (name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String accountNumber;
    @Column(unique = true)
    private long userId;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0.0")
    private BigDecimal balance;
    @Column(unique = true)
    private String cvu;
    @Column(unique = true)
    private String alias;


    @PrePersist
    public void prePersist() {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}
