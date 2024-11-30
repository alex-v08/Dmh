package com.dmh.accountservice.controller;

import com.dmh.accountservice.entity.Account;
import com.dmh.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account", description = "Account management endpoints")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Create a new account", description = "Creates a new account for a given user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ACCOUNT_ADMIN')")
    public ResponseEntity<Void> createAccount(
            @Parameter(description = "ID of the user to create account for", required = true)
            @RequestParam Long userId) {
        try {
            accountService.createAccount(userId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get account by user ID", description = "Returns an account for the specified user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ACCOUNT_ADMIN')")
    public ResponseEntity<Account> getAccountByUserId(
            @Parameter(description = "ID of the user to retrieve account for", required = true)
            @PathVariable Long userId) {
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

    @Operation(summary = "Update account balance", description = "Updates the balance of an account for the specified user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{userId}/balance")
    public ResponseEntity<Account> updateBalance(
            @Parameter(description = "ID of the user whose account balance to update", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Amount to update (can be positive or negative)", required = true)
            @RequestParam BigDecimal amount) {
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