package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.comment.CommentResponseDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * REST controller for managing plants.
 * Provides endpoints for uploading, updating, deleting, and retrieving plant information.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/plant")
public class PlantController {
    private final PlantService plantService;
    private final NoteService noteService;

    public PlantController(PlantService plantService, NoteService noteService) {
        this.plantService = plantService;
        this.noteService = noteService;
    }

    /**
     * Uploads a new plant along with its image.
     *
     * @param requestDTO The DTO containing the plant details and the image to be uploaded.
     * @return A ResponseEntity containing the created PlantResponseDTO or an error message.
     */
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

    /**
     * Retrieves a list of all plants.
     *
     * @return A ResponseEntity containing a list of PlantResponseDTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<PlantResponseDTO>> showAll() {
        List<PlantResponseDTO> plants = plantService.getPlants();
        return ResponseEntity.ok(plants);
    }

    /**
     * Updates an existing plant by its ID.
     *
     * @param plantId    The ID of the plant to update.
     * @param requestDTO The DTO containing the updated plant details.
     * @return A ResponseEntity containing the updated PlantResponseDTO or an error message.
     */
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

    /**
     * Deletes a plant by its ID.
     *
     * @param plantId The ID of the plant to delete.
     * @return A ResponseEntity indicating the result of the operation.
     */
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

    /**
     * Updates the watering and fertilizing times for an existing plant by its ID.
     *
     * @param plantId         The ID of the plant to update.
     * @param wateringTime    The new watering time for the plant.
     * @param fertilizingTime The new fertilizing time for the plant.
     * @return A ResponseEntity containing the updated PlantResponseDTO or an error message.
     */
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

    @GetMapping("/friends-plants/{userId}")
    public ResponseEntity<?> getFriendsPlants(@PathVariable Long userId) {
        try {
            List<PlantResponseDTO> plants = plantService.getFriendsPlants(userId);
            return new ResponseEntity<>(plants, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/friends-plants-details/{userId}")
    public ResponseEntity<?> getFriendsPlantsNotes(@PathVariable Long userId) {
        try {
            List<NoteResponseDTO> notes = plantService.getFriendsPlantsNotes(userId);
            List<CommentResponseDTO> comments = plantService.getFriendsPlantsComments(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("notes", notes);
            response.put("comments", comments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
