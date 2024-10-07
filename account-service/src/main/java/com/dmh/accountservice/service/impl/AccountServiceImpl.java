package com.dmh.accountservice.service.impl;

import com.dmh.accountservice.entity.Account;
import com.dmh.accountservice.entity.dto.AccountDto;
import com.dmh.accountservice.entity.dto.UserDto;
import com.dmh.accountservice.repository.AccountRepository;
import com.dmh.accountservice.repository.feign.FeignAliasGenerateRepository;
import com.dmh.accountservice.repository.feign.FeignCvuGenerateRepository;
import com.dmh.accountservice.repository.feign.FeignUserRepository;
import com.dmh.accountservice.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FeignAliasGenerateRepository aliasGenerateRepository;

    @Autowired
    private FeignCvuGenerateRepository cvuGenerateRepository;

    @Autowired
    private FeignUserRepository userRepository;

    @Override
    public Account createAccount (AccountDto accountDto) {
        UserDto userDto = userRepository.findById (accountDto.getUserId());
        if (userDto == null) {
            throw new RuntimeException("User not found");
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);

        account.setUserId(userDto.getId());
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setCvu(cvuGenerateRepository.generateCvu());
        account.setAlias(aliasGenerateRepository.generateAlias());

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }

        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByUserId (Long userId) {
        return accountRepository.getAccountByUserId(userId);
    }

    @Override
    public Account updateBalance (Long userId, BigDecimal amount) {
        Account account = getAccountByUserId(userId);
        if (account != null) {
            account.setBalance(account.getBalance().add(amount));
            return accountRepository.save(account);
        }
        throw new RuntimeException("Account not found");
    }
    }

