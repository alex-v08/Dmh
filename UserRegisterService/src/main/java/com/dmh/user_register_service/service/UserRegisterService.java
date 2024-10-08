package com.dmh.user_register_service.service;

import com.dmh.user_register_service.entity.dto.UserDto;
import com.dmh.user_register_service.entity.dto.UserRegisterDto;
import com.dmh.user_register_service.exception.UserAlreadyExistsException;
import com.dmh.user_register_service.repository.FeignAccountServiceClient;
import com.dmh.user_register_service.repository.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserServiceClient userServiceClient;
    private final FeignAccountServiceClient accountServiceClient;

    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(userRegisterDto.getFirstName());
        userDto.setLastName(userRegisterDto.getLastName());
        userDto.setEmail(userRegisterDto.getEmail());
        userDto.setDni(userRegisterDto.getDni());
        userDto.setPassword(userRegisterDto.getPassword());

        try {
            // Primero, crear el usuario
            UserDto createdUser = userServiceClient.createUser(userDto);

            // Luego, crear la cuenta asociada
            accountServiceClient.createAccount(createdUser.getId());

            return createdUser;
        } catch (Exception e) {
            if (e.getMessage().contains("Email already in use") || e.getMessage().contains("DNI already in use")) {
                throw new UserAlreadyExistsException(e.getMessage());
            }
            throw new RuntimeException("Error durante el registro de usuario", e);
        }
    }
}