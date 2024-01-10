package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPlantId(Long plantId);
}
