package com.dmh.user_register_service.service;

import com.dmh.user_register_service.entity.UserDto;
import com.dmh.user_register_service.repository.GenerateAliasClient;
import com.dmh.user_register_service.repository.GenerateCvuClient;
import com.dmh.user_register_service.repository.UserServiceClient;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {

    @Autowired
    private GenerateCvuClient generateCvuClient;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private GenerateAliasClient generateAliasClient;

    public UserDto registerUser(UserDto userDto) {
        userDto.setCvu(generateCvuClient.generateCvu());
        userDto.setAlias(generateAliasClient.generateAlias());

        return userServiceClient.createUser(userDto);
    }

}
