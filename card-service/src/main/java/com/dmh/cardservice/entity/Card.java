package com.dmh.cardservice.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private Integer cod;

    @Column(nullable = false)
    private String expirationDate; // Formato MM/YYYY

    @Column(nullable = false)
    private String firstLastName;

    @Column(nullable = false, unique = true)
    private Long numberId;

}
