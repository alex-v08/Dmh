package com.dmh.UserService.service.imp;

import com.dmh.UserService.dto.UserDTO;
import com.dmh.UserService.entity.User;
import com.dmh.UserService.repository.UserRepository;
import com.dmh.UserService.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    @Autowired
    private final UserRepository userRepository;



    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByDni(userDTO.getDni())) {
            throw new RuntimeException("User already exists");
        }
        try {
            User user = new User();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setCvu(userDTO.getCvu());
            user.setAlias(userDTO.getAlias());
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    public User updateUser(UserDTO userDTO) {

        User user = userRepository.getUserByEmail(userDTO.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCvu(userDTO.getCvu());
        user.setAlias(userDTO.getAlias());
        return userRepository.save(user);

    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
