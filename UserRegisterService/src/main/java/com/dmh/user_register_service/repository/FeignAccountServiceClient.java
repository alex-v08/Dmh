package com.dmh.user_register_service.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "localhost:8082")
public interface FeignAccountServiceClient {

    @PostMapping("/accounts")
    ResponseEntity<Void> createAccount(@RequestBody Long userId);
}
