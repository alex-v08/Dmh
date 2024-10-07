package com.dmh.accountservice.repository;

import com.dmh.accountservice.entity.Account;
import com.dmh.accountservice.entity.dto.AccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByUserId(Long userId);

}
