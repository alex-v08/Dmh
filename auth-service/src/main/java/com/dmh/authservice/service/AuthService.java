package com.dmh.authservice.service;

import com.dmh.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;


    public String login(String username, String password) {


        if ("user".equals(username) && "password".equals(password)) {
            return jwtUtil.generateToken(username);
        }
        return null;
    }

    public void logout(String token) {
        // En una implementación real, aquí invalidarías el token
        // Por ejemplo, añadiéndolo a una lista negra en una base de datos


    }
}

