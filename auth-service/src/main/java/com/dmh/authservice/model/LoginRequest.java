package com.dmh.authservice.model;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data

public class LoginRequest {
    private String username;
    private String password;


}
