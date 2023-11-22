package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/plant")
public class PlantController {
    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
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

}
