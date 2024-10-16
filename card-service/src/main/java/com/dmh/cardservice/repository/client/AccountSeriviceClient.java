package com.dmh.cardservice.repository.client;


import com.dmh.cardservice.entity.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service")
public interface AccountSeriviceClient {
    @GetMapping("/api/accounts/{accountId}")
    AccountDto getAccountById(@PathVariable Long accountId);

}
