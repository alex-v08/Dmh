package com.dmh.cardservice.entity.dto;

import lombok.Data;

@Data
public class CardDto {
    private Long id;
    private Long accountId;
    private Integer cod;
    private String expirationDate;
    private String firstLastName;
    private Long numberId;
}
