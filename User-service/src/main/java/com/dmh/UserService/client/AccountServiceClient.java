package com.dmh.UserService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "localhost:8082")
public interface AccountServiceClient {

    @PostMapping("/api/accounts")
    void createAccount(@RequestParam Long userId);
}