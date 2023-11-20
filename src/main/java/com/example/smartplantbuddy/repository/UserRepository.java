package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
}
