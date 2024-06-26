package com.dmh.user_register_service.entity;

import lombok.Data;

@Data

public class UserRegister {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String cvu;
    private String alias;

}
