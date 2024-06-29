package com.dmh.user_register_service.controller;


import com.dmh.user_register_service.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dmh.user_register_service.entity.UserDto;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor

public class UserRegisterController {
    @Autowired
    private final UserRegisterService userRegisterService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userRegisterService.registerUser(userDto));
    }
}
