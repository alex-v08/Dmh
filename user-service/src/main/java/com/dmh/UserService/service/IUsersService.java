package com.dmh.UserService.service;

import com.dmh.UserService.dto.UserDto;
import com.dmh.UserService.entity.Users;

import java.util.List;

public interface IUsersService {
    Users save(Users user);
    List<Users> findAll();
    Users findById(Long id);
    Users findByEmail(String email);
    Users update(Users user);
    void delete(Long id);
}