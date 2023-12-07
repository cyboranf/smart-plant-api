package com.example.smartplantbuddy.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String login;
    private Set<String> roles;
    private Set<Long> plantIds;
}
