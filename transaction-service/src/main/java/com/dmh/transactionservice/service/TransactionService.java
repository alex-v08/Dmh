package com.dmh.transactionservice.service;

import com.dmh.transactionservice.dto.TransactionRequest;
import com.dmh.transactionservice.dto.TransferenceRequest;
import com.dmh.transactionservice.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createDeposit(Long accountId, TransactionRequest request);
    Transaction createTransference(Long accountId, TransferenceRequest request);
    List<Transaction> getTransactionsByAccountId(Long accountId);
}
