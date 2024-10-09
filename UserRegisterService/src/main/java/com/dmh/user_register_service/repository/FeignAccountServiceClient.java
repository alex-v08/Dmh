package com.dmh.user_register_service.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "localhost:8082")
public interface FeignAccountServiceClient {

    @PostMapping("/accounts")
    void createAccount(@RequestParam Long userId);
}
