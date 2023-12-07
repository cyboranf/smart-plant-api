package com.example.smartplantbuddy.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDTO {
    private String login;
    private String password;
    private Set<Long> roleIds;
    private Set<Long> plantIds;
}
