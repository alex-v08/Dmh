package com.dmh.user_register_service.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String dni;
    private String password;

}