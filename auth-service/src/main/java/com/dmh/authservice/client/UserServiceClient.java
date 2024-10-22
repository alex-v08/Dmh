package com.dmh.authservice.client;

import com.dmh.authservice.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/email/{email}")
    UserDTO findByEmail(@PathVariable String email);

    @GetMapping("/api/users/{id}")
    UserDTO findById(@PathVariable Long id);
}
