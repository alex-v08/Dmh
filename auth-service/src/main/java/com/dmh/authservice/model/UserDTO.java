package com.dmh.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String dni;

    @JsonIgnore
    private String password;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}