package com.dmh.accountservice.service.impl;

import com.dmh.accountservice.entity.Account;
import com.dmh.accountservice.repository.AccountRepository;
import com.dmh.accountservice.repository.client.FeignAliasGenerateRepository;
import com.dmh.accountservice.repository.client.FeignCvuGenerateRepository;
import com.dmh.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final FeignAliasGenerateRepository aliasGenerateRepository;
    private final FeignCvuGenerateRepository cvuGenerateRepository;

    @Override
    public void createAccount(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setCvu(cvuGenerateRepository.generateCvu());
        account.setAlias(aliasGenerateRepository.generateAlias());
        accountRepository.save(account);
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        return accountRepository.getAccountByUserId(userId);
    }

    @Override
    public Account updateBalance(Long userId, BigDecimal amount) {
        Account account = getAccountByUserId(userId);
        if (account == null) {
            throw new RuntimeException("Account not found for user ID: " + userId);
        }
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }
}