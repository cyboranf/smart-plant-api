package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.dto.register.RegisterResponseDTO;
import com.example.smartplantbuddy.exception.register.PasswordNotMatchingException;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegisterMapper(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User toEntity(RegisterRequestDTO requestDTO) {
        User user = new User();
        user.setLogin(requestDTO.getLogin());

        if (requestDTO.getPassword().equals(requestDTO.getMatchingPassword())) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            return userRepository.save(user);
        } else {
            throw new PasswordNotMatchingException("Password field and Matching Password field doesn't match.");
        }
    }

    public RegisterResponseDTO toDTO(User user) {
        RegisterResponseDTO responseDTO = new RegisterResponseDTO();
        responseDTO.setLogin(user.getLogin());
        return responseDTO;
    }
}
