package com.dmh.UserService.controller;

import com.dmh.UserService.dto.UserDto;
import com.dmh.UserService.entity.Users;
import com.dmh.UserService.service.IUsersService;
import com.dmh.UserService.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Create a new user with a new account")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        Users user = userMapper.userDtoToUser(userDto);
        Users createdUser = usersService.save(user);
        return new ResponseEntity<>(userMapper.userToUserDto(createdUser), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<Users> users = usersService.findAll();
        List<UserDto> userDtos = users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user data")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Users user = usersService.findById(id);
        return user != null ? ResponseEntity.ok(userMapper.userToUserDto(user)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Users user = usersService.findByEmail(email);
        return user != null ? ResponseEntity.ok(userMapper.userToUserDto(user)) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user data")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        Users existingUser = usersService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.updateUserFromDto(userDto, existingUser);
        Users updatedUser = usersService.update(existingUser);
        return ResponseEntity.ok(userMapper.userToUserDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}