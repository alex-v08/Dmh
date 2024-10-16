package com.dmh.transactionservice.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferenceRequest {
    private BigDecimal amount;
    private String dated;
    private String description;
}
