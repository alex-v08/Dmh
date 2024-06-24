package com.dmh.UserService.service;

import com.dmh.UserService.dto.UserDTO;
import com.dmh.UserService.entity.User;

public interface IUserService {

    User createUser(UserDTO userDTO);

    User updateUser(UserDTO userDTO);




}
