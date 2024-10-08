package com.dmh.accountservice.repository.feign;


import com.dmh.accountservice.entity.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserService", url = "localhost:8081")
public interface FeignUserRepository {
    @GetMapping("/email/{email}")
    UserDto findByEmail(@PathVariable String email);

    @GetMapping("/idUser/{id}")
    UserDto findById(@PathVariable Long id);

    @GetMapping("/users/{userId}")
    UserDto getUserById (Long userId);
}
