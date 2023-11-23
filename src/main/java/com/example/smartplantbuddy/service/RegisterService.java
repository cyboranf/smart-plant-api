package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.dto.register.RegisterResponseDTO;
import com.example.smartplantbuddy.mapper.RegisterMapper;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.validation.RegisterValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RegisterService {
    private final UserRepository userRepository;
    private final RegisterMapper registerMapper;
    private final RegisterValidator registerValidator;

    public RegisterService(UserRepository userRepository, RegisterMapper registerMapper, RegisterValidator registerValidator) {
        this.userRepository = userRepository;
        this.registerMapper = registerMapper;
        this.registerValidator = registerValidator;
    }

    /**
     * @param requestDTO
     * @return DTO of new account
     */
    public RegisterResponseDTO createAccount(RegisterRequestDTO requestDTO) {
        registerValidator.registerValidation(requestDTO);
        User user = registerMapper.toEntity(requestDTO);
        User savedUser = userRepository.save(user);
        return registerMapper.toDTO(savedUser);
    }
}
