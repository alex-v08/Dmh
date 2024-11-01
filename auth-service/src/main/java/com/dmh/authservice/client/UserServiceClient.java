package com.dmh.authservice.client;


import com.dmh.authservice.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${user-service.url:http://localhost:8081}"
)
public interface UserServiceClient {  // Debe ser una interface, no una clase

    @GetMapping("/api/users/email/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable("email") String email);

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id);
}