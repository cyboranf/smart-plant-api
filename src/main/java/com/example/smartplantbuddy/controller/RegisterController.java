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

@RestController
@RequestMapping("/api")
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO responseDTO = registerService.createAccount(registerRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
