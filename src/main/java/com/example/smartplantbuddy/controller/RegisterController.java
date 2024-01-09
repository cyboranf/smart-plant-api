package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.dto.register.RegisterResponseDTO;
import com.example.smartplantbuddy.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user registration operations.
 * Provides an endpoint for new users to register in the system.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api")
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * Registers a new user in the system.
     *
     * @param registerRequestDTO The DTO containing the user registration details.
     * @return A ResponseEntity with the registered user's details or an error message.
     */
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO responseDTO = registerService.createAccount(registerRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
