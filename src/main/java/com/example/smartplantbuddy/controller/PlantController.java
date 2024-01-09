package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.service.NoteService;
import com.example.smartplantbuddy.service.PlantService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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

    @PutMapping("/update/{plantId}")
    public ResponseEntity<?> updatePlant(@PathVariable Long plantId, @ModelAttribute PlantRequestDTO requestDTO) {
        try {
            PlantResponseDTO updatedPlant = plantService.updatePlant(plantId, requestDTO);
            return new ResponseEntity<>(updatedPlant, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Plant not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{plantId}")
    public ResponseEntity<?> deletePlant(@PathVariable Long plantId) {
        try {
            plantService.deletePlant(plantId);
            return new ResponseEntity<>("Plant deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Plant not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update-times/{plantId}")
    public ResponseEntity<?> updatePlantTimes(@PathVariable Long plantId,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime wateringTime,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fertilizingTime) {
        try {
            PlantResponseDTO updatedPlant = plantService.updatePlantTimes(plantId, wateringTime, fertilizingTime);
            return new ResponseEntity<>(updatedPlant, HttpStatus.OK);
        } catch (PlantNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
