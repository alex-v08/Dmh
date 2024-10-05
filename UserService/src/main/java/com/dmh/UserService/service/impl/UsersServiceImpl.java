package com.dmh.UserService.service.impl;

import com.dmh.UserService.dto.UserDto;
import com.dmh.UserService.entity.Users;
import com.dmh.UserService.exception.UserAlreadyExistsException;
import com.dmh.UserService.repository.UsersRepository;
import com.dmh.UserService.service.IUsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    @Transactional
    public Users save(UserDto userDto) {
        if (usersRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (usersRepository.findByDni(userDto.getDni()) != null) {
            throw new UserAlreadyExistsException("DNI already in use");
        }
        Users user = new Users();
        BeanUtils.copyProperties(userDto, user);
        return usersRepository.save(user);
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
    public Users update(Long id, UserDto userDto) {
        Optional<Users> existingUser = usersRepository.findById(id);
        if (existingUser.isPresent()) {
            Users user = existingUser.get();
            if (!user.getEmail().equals(userDto.getEmail()) && usersRepository.findByEmail(userDto.getEmail()) != null) {
                throw new UserAlreadyExistsException("Email already in use");
            }
            if (!user.getDni().equals(userDto.getDni()) && usersRepository.findByDni(userDto.getDni()) != null) {
                throw new UserAlreadyExistsException("DNI already in use");
            }
            BeanUtils.copyProperties(userDto, user);
            user.setId(id);
            return usersRepository.save(user);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }
}