package com.dmh.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;


    private String firstName;

    private String lastName;


    private String email;

    private String dni;

    @JsonIgnore // No se incluye en las respuestas JSON
    private String password;

    // Constructor para crear un DTO con datos básicos
    public UserDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    // Método helper para obtener el nombre completo
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
