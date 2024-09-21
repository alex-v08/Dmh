package com.dmh.user_register_service.service;

import com.dmh.user_register_service.entity.UserDto;
import com.dmh.user_register_service.entity.dto.UserRegisterDto;
import com.dmh.user_register_service.exception.UserAlreadyExistsException;
import com.dmh.user_register_service.repository.GenerateAliasClient;
import com.dmh.user_register_service.repository.GenerateCvuClient;
import com.dmh.user_register_service.repository.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final GenerateCvuClient generateCvuClient;
    private final GenerateAliasClient generateAliasClient;
    private final UserServiceClient userServiceClient;

    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        // Generate CVU and Alias
        String cvu = generateCvuClient.generateCvu();
        String alias = generateAliasClient.generateAlias();

        // Create UserDto object
        UserDto userDto = new UserDto();
        userDto.setFirstName(userRegisterDto.getFirstName());
        userDto.setLastName(userRegisterDto.getLastName());
        userDto.setEmail(userRegisterDto.getEmail());
        userDto.setDni(userRegisterDto.getDni());
        userDto.setPassword(userRegisterDto.getPassword());
        userDto.setCvu(cvu);
        userDto.setAlias(alias);

        try {

            return userServiceClient.createUser(userDto);
        } catch (Exception e) {

            if (e.getMessage().contains("Email already in use") || e.getMessage().contains("DNI already in use")) {
                throw new UserAlreadyExistsException(e.getMessage());
            }
            throw e;
        }
    }
}