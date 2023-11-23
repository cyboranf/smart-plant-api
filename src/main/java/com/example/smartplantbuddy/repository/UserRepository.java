package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

    Optional<User> findByLogin(String login);
}
