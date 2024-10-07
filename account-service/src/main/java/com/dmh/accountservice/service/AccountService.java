package com.dmh.accountservice.service;

import com.dmh.accountservice.entity.Account;
import com.dmh.accountservice.entity.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface AccountService {
    Account createAccount(AccountDto accountDto);
    Account getAccountByUserId(Long userId);
    Account updateBalance(Long userId, BigDecimal amount);
}
