package com.example.smartplantbuddy.validation;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.exception.register.ExistingLoginException;
import com.example.smartplantbuddy.exception.register.InvalidLoginException;
import com.example.smartplantbuddy.exception.register.InvalidPasswordException;
import com.example.smartplantbuddy.exception.register.PasswordNotMatchingException;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegisterValidator {
    private final UserRepository userRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,50}$");

    public RegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerValidation(RegisterRequestDTO registerRequestDTO) {
        if (!userRepository.findByLogin(registerRequestDTO.getLogin()).isEmpty()) {
            throw new ExistingLoginException("Account with login: " + registerRequestDTO.getLogin() + " is already exists.");
        }
        if (registerRequestDTO.getLogin().length() <= 6 || registerRequestDTO.getLogin().length() > 12) {
            throw new InvalidLoginException("Login must have between 6 and 12 characters length.");
        }
        if (!PASSWORD_PATTERN.matcher(registerRequestDTO.getPassword()).matches()) {
            throw new InvalidPasswordException("Password must be between 8 and 50 characters, include at least one special character and one number.");
        }
        if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getMatchingPassword())) {
            throw new PasswordNotMatchingException("Password field and Matching Password field are not the same.");
        }
    }
}
