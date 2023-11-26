package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.dto.register.RegisterResponseDTO;

import com.example.smartplantbuddy.exception.register.PasswordNotMatchingException;
import com.example.smartplantbuddy.model.Role;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.RoleRepository;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RegisterMapper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RegisterMapper(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User toEntity(RegisterRequestDTO requestDTO) {
        User user = new User();
        user.setLogin(requestDTO.getLogin());

        if (requestDTO.getPassword().equals(requestDTO.getMatchingPassword())) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            return userRepository.save(user);
        } else {
            throw new PasswordNotMatchingException("Password field and Matching Password field don't match.");
        }
    }

    public RegisterResponseDTO toDTO(User user) {
        RegisterResponseDTO responseDTO = new RegisterResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setLogin(user.getLogin());
        return responseDTO;
    }
}
