package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.service.NoteService;
import com.example.smartplantbuddy.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant")
public class PlantController {
    private final PlantService plantService;
    private final NoteService noteService;
    public PlantController(PlantService plantService, NoteService noteService) {
        this.plantService = plantService;
        this.noteService = noteService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPlant(@ModelAttribute PlantRequestDTO requestDTO) {
        try {
            PlantResponseDTO responseDTO = plantService.uploadImages(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (ImageEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlantResponseDTO>> showAll() {
        List<PlantResponseDTO> plants = plantService.getPlants();
        return ResponseEntity.ok(plants);
    }
    @PostMapping("/addNote")
    public ResponseEntity<?> addNoteToPlant(@ModelAttribute NoteRequestDTO requestDTO) {
        try {
            NoteResponseDTO responseDTO = noteService.addNoteToPlant(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{plantId}/notes")
    public ResponseEntity<?> getAllNotesForPlant(@PathVariable Long plantId) {
        try {
            List<NoteResponseDTO> notes = noteService.findAllNotesByPlantId(plantId);
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
