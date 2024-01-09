package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.user.UserResponseDTO;
import com.example.smartplantbuddy.exception.user.UsernameNotFoundException;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.model.Role;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * REST controller for managing user data.
 * Provides an endpoint to check if a user exists based on the authentication context.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    /**
     * Checks if the current authenticated user exists in the system.
     *
     * @return A ResponseEntity with the user's details if found, or throws an exception if not found.
     */
    @GetMapping("/isUser")
    public ResponseEntity<?> isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with given username not found"));
        return ResponseEntity.ok(mapToUserResponseDTO(user));
    }
    /**
     * Maps a User entity to a UserResponseDTO.
     *
     * @param user The User entity to map.
     * @return A UserResponseDTO representing the user.
     */
    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setLogin(user.getLogin());
        userResponseDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        userResponseDTO.setPlantIds(user.getPlants().stream().map(Plant::getId).collect(Collectors.toSet()));
        return userResponseDTO;
    }
}
