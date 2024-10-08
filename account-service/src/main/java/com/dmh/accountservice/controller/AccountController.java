package com.dmh.accountservice.controller;

import com.dmh.accountservice.entity.Account;
import com.dmh.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Void> createAccount(@RequestBody Long userId) {
        try {
            accountService.createAccount(userId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccountByUserId(@PathVariable Long userId) {
        try {
            Account account = accountService.getAccountByUserId(userId);
            if (account != null) {
                return new ResponseEntity<>(account, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/balance")
    public ResponseEntity<Account> updateBalance(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        try {
            Account updatedAccount = accountService.updateBalance(userId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}