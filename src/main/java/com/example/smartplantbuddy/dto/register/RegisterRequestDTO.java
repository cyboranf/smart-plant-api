package com.example.smartplantbuddy.dto.register;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String login;
    private String password;
    private String matchingPassword;

}
