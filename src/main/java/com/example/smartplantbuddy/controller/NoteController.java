package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing notes related to plants.
 * Provides endpoints for adding, updating, deleting, and retrieving notes for a specific plant.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Adds a new note to a plant.
     *
     * @param requestDTO The DTO containing the details of the note to be added.
     * @return A ResponseEntity containing the created NoteResponseDTO or an error message.
     */
    @PostMapping("/addNote")
    public ResponseEntity<?> addNoteToPlant(@ModelAttribute NoteRequestDTO requestDTO) {
        try {
            NoteResponseDTO responseDTO = noteService.addNoteToPlant(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all notes associated with a specific plant.
     *
     * @param plantId The ID of the plant for which notes are to be retrieved.
     * @return A ResponseEntity containing a list of NoteResponseDTOs or an error message.
     */
    @GetMapping("/{plantId}/notes")
    public ResponseEntity<?> getAllNotesForPlant(@PathVariable Long plantId) {
        try {
            List<NoteResponseDTO> notes = noteService.findAllNotesByPlantId(plantId);
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing note by its ID.
     *
     * @param noteId The ID of the note to update.
     * @param requestDTO The DTO containing the updated details of the note.
     * @return A ResponseEntity containing the updated NoteResponseDTO or an error message.
     */
    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable Long noteId, @ModelAttribute NoteRequestDTO requestDTO) {
        try {
            NoteResponseDTO responseDTO = noteService.updateNote(noteId, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a note by its ID.
     *
     * @param noteId The ID of the note to delete.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId) {
        try {
            noteService.deleteNote(noteId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
