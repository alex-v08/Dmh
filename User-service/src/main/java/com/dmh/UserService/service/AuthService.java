package com.dmh.UserService.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8085")
public interface AuthService {

    @PostMapping("/validate")
    boolean validateToken(@RequestBody String token);

    @PostMapping("/username")
    String getUsernameFromToken(@RequestBody String token);
}