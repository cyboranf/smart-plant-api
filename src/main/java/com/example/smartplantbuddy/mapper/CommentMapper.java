package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.comment.CommentRequestDTO;
import com.example.smartplantbuddy.dto.comment.CommentResponseDTO;
import com.example.smartplantbuddy.exception.note.NoteNotFoundException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.model.Comment;
import com.example.smartplantbuddy.model.Note;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.NoteRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    private final PlantRepository plantRepository;
    private final NoteRepository noteRepository;

    public CommentMapper(PlantRepository plantRepository, NoteRepository noteRepository) {
        this.plantRepository = plantRepository;
        this.noteRepository = noteRepository;
    }

    public Comment toEntity(CommentRequestDTO commentRequestDTO) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDTO.getContent());
        comment.setTimestamp(LocalDateTime.now());

        Plant plant = plantRepository.findById(commentRequestDTO.getPlantId())
                .orElseThrow(() -> new PlantNotFoundException("Plant not found with id " + commentRequestDTO.getPlantId()));
        comment.setPlant(plant);

        if (commentRequestDTO.getNoteId() != null) {
            Note note = noteRepository.findById(commentRequestDTO.getNoteId())
                    .orElseThrow(() -> new NoteNotFoundException("Note not found with id " + commentRequestDTO.getNoteId()));
            comment.setNote(note);
        }

        return comment;
    }



    public CommentResponseDTO toDTO(Comment comment) {
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setContent(comment.getContent());
        commentResponseDTO.setTimestamp(comment.getTimestamp().toString());
        commentResponseDTO.setUserId(comment.getUser().getId());
        commentResponseDTO.setUsername(comment.getUser().getLogin());
        commentResponseDTO.setPlantId(comment.getPlant().getId());

        // Check if the comment is associated with a note
        if (comment.getNote() != null) {
            commentResponseDTO.setNoteId(comment.getNote().getId());
        } else {
            commentResponseDTO.setNoteId(null);
        }

        return commentResponseDTO;
    }
}
