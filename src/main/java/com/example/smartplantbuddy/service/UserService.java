package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
/**
 * Service layer for managing user data.
 * This service is responsible for user-related operations such as querying user details,
 * updating user profiles, and other user management tasks.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
