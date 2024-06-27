package com.dmh.user_register_service.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "GenerateAlias", url = "localhost:8087")
public interface GenerateAliasClient {

    @GetMapping("/generateAlias")
    String generateAlias();
}
