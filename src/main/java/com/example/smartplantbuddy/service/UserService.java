package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.exception.user.UsernameNotFoundException;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
