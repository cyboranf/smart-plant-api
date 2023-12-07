package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByPlantId(Long plantId);
}
