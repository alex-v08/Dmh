package com.dmh.UserService.service.impl;


import com.dmh.UserService.client.AccountServiceClient;
import com.dmh.UserService.entity.Users;
import com.dmh.UserService.exception.UserAlreadyExistsException;
import com.dmh.UserService.repository.UsersRepository;
import com.dmh.UserService.service.IUsersService;
import com.dmh.UserService.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Override
    @Transactional
    public Users save(Users user) {
        if (usersRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (usersRepository.findByDni(user.getDni()) != null) {
            throw new UserAlreadyExistsException("DNI already in use");
        }

        Users savedUser = usersRepository.save(user);

        try {
            accountServiceClient.createAccount(savedUser.getId());
        } catch (Exception e) {
            usersRepository.delete(savedUser);
            throw new RuntimeException("Failed to create account for user", e);
        }

        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Users findById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Users update(Users user) {
        Optional<Users> existingUser = usersRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            Users currentUser = existingUser.get();
            if (!currentUser.getEmail().equals(user.getEmail()) && usersRepository.findByEmail(user.getEmail()) != null) {
                throw new UserAlreadyExistsException("Email already in use");
            }
            if (!currentUser.getDni().equals(user.getDni()) && usersRepository.findByDni(user.getDni()) != null) {
                throw new UserAlreadyExistsException("DNI already in use");
            }

            userMapper.updateUserFromDto(userMapper.userToUserDto(user), currentUser);
            return usersRepository.save(currentUser);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }
}