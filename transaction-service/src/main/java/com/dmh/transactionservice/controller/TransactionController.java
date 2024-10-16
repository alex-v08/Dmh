package com.dmh.transactionservice.controller;

import com.dmh.transactionservice.dto.TransactionRequest;
import com.dmh.transactionservice.dto.TransferenceRequest;
import com.dmh.transactionservice.entity.Transaction;
import com.dmh.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{account_id}")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping("/deposits")
    public ResponseEntity<Transaction> createDeposit(
            @PathVariable("account_id") Long accountId,
            @RequestBody TransactionRequest request) {
        Transaction deposit = transactionService.createDeposit(accountId, request);
        return new ResponseEntity<>(deposit, HttpStatus.CREATED);
    }

    @PostMapping("/transferences")
    public ResponseEntity<Transaction> createTransference(
            @PathVariable("account_id") Long accountId,
            @RequestBody TransferenceRequest request) {
        Transaction transference = transactionService.createTransference(accountId, request);
        return new ResponseEntity<>(transference, HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("account_id") Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
