package com.dmh.transactionservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private BigDecimal amount;
    private String dated;
    private String description;
}
