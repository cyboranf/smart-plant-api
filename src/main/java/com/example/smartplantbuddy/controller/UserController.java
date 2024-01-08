package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.user.UserResponseDTO;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.model.Role;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @GetMapping("/isUser")
    public ResponseEntity<?> isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findUserByLogin(username);
            if (user != null) {
                UserResponseDTO userResponseDTO = mapToUserResponseDTO(user);
                return ResponseEntity.ok(userResponseDTO);
            }
        }
        return new ResponseEntity<String>("The String ResponseBody with custom status code (403 Forbidden)",
                HttpStatus.FORBIDDEN);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setLogin(user.getLogin());
        userResponseDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        userResponseDTO.setPlantIds(user.getPlants().stream().map(Plant::getId).collect(Collectors.toSet()));
        return userResponseDTO;
    }
}
