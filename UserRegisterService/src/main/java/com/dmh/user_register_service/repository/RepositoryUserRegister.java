package com.dmh.user_register_service.repository;


import com.dmh.user_register_service.entity.UserRegister;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserService", url = "http://localhost:8081/user")


public interface RepositoryUserRegister {
    @PostMapping("/users")
    UserRegister createUser(@RequestBody UserRegister userDTO);

    @PutMapping("/users/{id}")
    UserRegister updateUser(@PathVariable("id") Long id, @RequestBody UserRegister userDTO);
}




