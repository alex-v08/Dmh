package com.dmh.user_register_service.repository;


import com.dmh.user_register_service.entity.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "UserService", url = "localhost:8081")
public interface UserServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/users/createUser")
    UserDto createUser(@RequestBody UserDto userDto);

    @DeleteMapping("/users/deleteUser")
    void deleteUser(@RequestBody Long id);
}
