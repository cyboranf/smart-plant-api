package com.example.smartplantbuddy.validation;

import com.example.smartplantbuddy.dto.comment.CommentRequestDTO;
import com.example.smartplantbuddy.exception.comment.InvalidCommentException;
import com.example.smartplantbuddy.exception.note.NoteNotFoundException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.repository.CommentRepository;
import com.example.smartplantbuddy.repository.NoteRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

@Component
public class CommentValidator {
    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;
    private final PlantRepository plantRepository;

    public CommentValidator(CommentRepository commentRepository, NoteRepository noteRepository, PlantRepository plantRepository) {
        this.commentRepository = commentRepository;
        this.noteRepository = noteRepository;
        this.plantRepository = plantRepository;
    }

    public void commentRequestDTOValidation(CommentRequestDTO commentRequestDTO) {
        if (commentRequestDTO.getContent().length() > 100 || commentRequestDTO.getContent() == null) {
            throw new InvalidCommentException("Content of Comment should have >0 and 100> characters length");
        }
        plantRepository.findById(commentRequestDTO.getPlantId())
                .orElseThrow(() -> new PlantNotFoundException("Plant with id = " + commentRequestDTO.getPlantId() + " doesnt exists."));

        if (commentRequestDTO.getNoteId() != null) {
            noteRepository.findById(commentRequestDTO.getNoteId())
                    .orElseThrow(() -> new NoteNotFoundException("Can not found note with id = " + commentRequestDTO.getNoteId()));
        }
    }
    public void getCommentsByPlantIdValidation(Long plantId) {
        plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = "+ plantId));
    }
}
