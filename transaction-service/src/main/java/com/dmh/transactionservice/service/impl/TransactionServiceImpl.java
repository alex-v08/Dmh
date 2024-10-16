package com.dmh.transactionservice.service.impl;

import com.dmh.transactionservice.dto.TransactionRequest;
import com.dmh.transactionservice.dto.TransferenceRequest;
import com.dmh.transactionservice.entity.Transaction;
import com.dmh.transactionservice.mapper.TransactionMapper;
import com.dmh.transactionservice.repository.TransactionRepository;
import com.dmh.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public Transaction createDeposit(Long accountId, TransactionRequest request) {
        Transaction deposit = transactionMapper.toDepositTransaction(request, accountId);
        return transactionRepository.save(deposit);
    }

    @Override
    public Transaction createTransference(Long accountId, TransferenceRequest request) {
        Transaction transference = transactionMapper.toTransferenceTransaction(request, accountId);
        return transactionRepository.save(transference);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
