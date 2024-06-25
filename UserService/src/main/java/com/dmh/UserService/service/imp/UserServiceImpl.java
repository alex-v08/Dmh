package com.dmh.UserService.service.imp;

import com.dmh.UserService.dto.UserDTO;
import com.dmh.UserService.entity.User;
import com.dmh.UserService.repository.UserRepository;
import com.dmh.UserService.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{
    private final UserRepository usersRepository;


    public void createUser(UserDTO userDTO) {
        if (usersRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (usersRepository.existsByDni(userDTO.getDni())) {
            throw new RuntimeException("Dni already exists");
        }

        User user = new User();
        updateUser(userDTO, user.getId());


    }

    public void updateUser(UserDTO userDTO, Long id) {

        User user = usersRepository.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        usersRepository.save(user);
    }
}
