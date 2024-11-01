package com.dmh.authservice.service;

import com.dmh.authservice.client.UserServiceClient;
import com.dmh.authservice.exception.UserNotFoundException;
import com.dmh.authservice.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserServiceClient userServiceClient;

    public UserDTO getUserByEmail(String email) {
        ResponseEntity<UserDTO> response = userServiceClient.findByEmail(email);
        if (response.getBody() == null) {
            throw new UserNotFoundException ("Usuario no encontrado");
        }
        return response.getBody();
    }
}