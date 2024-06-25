package com.dmh.UserService.service.imp;

import com.dmh.UserService.dto.UserDTO;
import com.dmh.UserService.entity.User;
import com.dmh.UserService.repository.UserRepository;
import com.dmh.UserService.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    public void createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByDni(userDTO.getDni())) {
            throw new RuntimeException("Dni already exists");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCvu(userDTO.getCvu());
        user.setAlias(userDTO.getAlias());

        userRepository.save(user);
    }

    public void updateUser(UserDTO userDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Assumiendo que el password puede actualizarse
        user.setCvu(userDTO.getCvu());
        user.setAlias(userDTO.getAlias());

        userRepository.save(user);
    }
}
