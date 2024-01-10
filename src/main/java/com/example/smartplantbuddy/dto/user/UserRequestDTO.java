package com.example.smartplantbuddy.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDTO {
    private String login;

    private Set<Long> plantIds;
    private Set<Long> roleIds;
}
