package com.dmh.user_register_service.service;

import com.dmh.user_register_service.entity.dto.UserDto;
import com.dmh.user_register_service.entity.dto.UserRegisterDto;
import com.dmh.user_register_service.exception.UserAlreadyExistsException;
import com.dmh.user_register_service.repository.FeignAccountServiceClient;
import com.dmh.user_register_service.repository.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisterService.class);

    private final UserServiceClient userServiceClient;
    private final FeignAccountServiceClient accountServiceClient;


    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        logger.info("Starting user registration process for email: {}", userRegisterDto.getEmail());

        validateUserRegistrationData(userRegisterDto);

        UserDto userDto = mapToUserDto(userRegisterDto);

        try {
            UserDto createdUser = createUser(userDto);
            try {
                createAccount(createdUser.getId());
                return createdUser;
            } catch (Exception e) {
                logger.error("Error creating account for user ID: {}. Error: {}", createdUser.getId(), e.getMessage());
                rollbackUserCreation(createdUser.getId());
                throw new RuntimeException("Failed to create account. User creation rolled back.", e);
            }
        } catch (UserAlreadyExistsException e) {
            logger.warn("Attempted to create duplicate user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user registration: {}", e.getMessage());
            throw new RuntimeException("Error during user registration", e);
        }
    }

    private UserDto createUser(UserDto userDto) {
        try {
            userDto.setPassword(userDto.getPassword());
            UserDto createdUser = userServiceClient.createUser(userDto);
            logger.info("User created successfully with ID: {}", createdUser.getId());
            return createdUser;
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        }
    }

    private void createAccount(Long userId) {
        try {
            accountServiceClient.createAccount(userId);
            logger.info("Account created successfully for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error creating account for user ID: {}. Error: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to create account", e);
        }
    }

    private void validateUserRegistrationData(UserRegisterDto userRegisterDto) {
        if (userRegisterDto.getEmail() == null || userRegisterDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (userRegisterDto.getPassword() == null || userRegisterDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (userRegisterDto.getDni() == null || !userRegisterDto.getDni().matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI must be 8 digits");
        }
        // Add more validations as needed
    }

    private UserDto mapToUserDto(UserRegisterDto userRegisterDto) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(userRegisterDto.getFirstName());
        userDto.setLastName(userRegisterDto.getLastName());
        userDto.setEmail(userRegisterDto.getEmail());
        userDto.setDni(userRegisterDto.getDni());
        userDto.setPassword(userRegisterDto.getPassword());
        return userDto;
    }

    private void rollbackUserCreation(Long userId) {
        try {
            logger.info("Rolling back user creation for user ID: {}", userId);
            userServiceClient.deleteUser(userId);
            logger.info("User deletion successful for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error rolling back user creation for user ID: {}. Error: {}", userId, e.getMessage());

        }
    }
}