package com.dmh.user_register_service.entity.dto;

import lombok.Data;

@Data

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String dni;
    private String password;
    private String cvu;
    private String alias;

}
