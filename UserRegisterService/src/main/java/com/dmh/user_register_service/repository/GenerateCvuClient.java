package com.dmh.user_register_service.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "GenerateCvu", url = "localhost:8089")
public interface GenerateCvuClient {

    @GetMapping("/generateCvu")
    String generateCvu();
}
