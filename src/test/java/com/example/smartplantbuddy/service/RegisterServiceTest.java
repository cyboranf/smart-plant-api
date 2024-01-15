package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.dto.register.RegisterRequestDTO;
import com.example.smartplantbuddy.dto.register.RegisterResponseDTO;
import com.example.smartplantbuddy.mapper.RegisterMapper;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.validation.RegisterValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegisterMapper registerMapper;
    @Mock
    private RegisterValidator registerValidator;

    @InjectMocks
    private RegisterService registerService;

    private RegisterRequestDTO requestDTO;
    private RegisterResponseDTO responseDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        requestDTO = new RegisterRequestDTO();
        responseDTO = new RegisterResponseDTO();
        user = new User();
    }

    @Test
    public void testCreateAccount() {
        when(registerMapper.toEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(registerMapper.toDTO(user)).thenReturn(responseDTO);

        RegisterResponseDTO result = registerService.createAccount(requestDTO);

        assertNotNull(result);
        verify(registerValidator).registerValidation(requestDTO);
        verify(registerMapper).toEntity(requestDTO);
        verify(userRepository).save(user);
        verify(registerMapper).toDTO(user);
    }
}