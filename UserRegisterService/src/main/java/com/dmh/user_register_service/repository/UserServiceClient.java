package com.dmh.user_register_service.repository;


import com.dmh.user_register_service.entity.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserService", url = "localhost:8081")
public interface UserServiceClient {

    @GetMapping("/createUser")
    UserDto createUser(@RequestBody UserDto userDto);
}
