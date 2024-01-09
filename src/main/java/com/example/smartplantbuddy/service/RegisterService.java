package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.dto.register.RegisterResponseDTO;
import com.example.smartplantbuddy.mapper.RegisterMapper;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.validation.RegisterValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
/**
 * Service layer for handling user registration.
 * This service manages the creation of new user accounts based on registration requests.
 * It involves validating the registration details and persisting the new user to the database.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
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
     * Creates a new user account based on the registration request data.
     * The registration data is validated before the account is created.
     *
     * @param requestDTO The DTO containing the registration details.
     * @return A DTO representing the newly created user account.
     */
    public RegisterResponseDTO createAccount(RegisterRequestDTO requestDTO) {
        registerValidator.registerValidation(requestDTO);
        User user = registerMapper.toEntity(requestDTO);
        User savedUser = userRepository.save(user);
        return registerMapper.toDTO(savedUser);
    }

}
