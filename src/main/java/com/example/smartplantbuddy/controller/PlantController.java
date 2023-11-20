package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.model.enums.Light;
import com.example.smartplantbuddy.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/plant")
public class PlantController {
    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping("/upload")
    public ResponseEntity<PlantResponseDTO> uploadPlant(@RequestParam("name") String name,
                                                        @RequestParam("plantImage") MultipartFile plantImage,
                                                        @RequestParam("wateringTime") String wateringTime,
                                                        @RequestParam("wateringFrequency") String wateringFrequency,
                                                        @RequestParam("lightAccess") String lightAccess,
                                                        @RequestParam("lightScore") int lightScore) {
        try {
            PlantRequestDTO requestDTO = new PlantRequestDTO();
            requestDTO.setName(name);
            requestDTO.setPlantImage(plantImage);
            requestDTO.setWateringTime(LocalDateTime.parse(wateringTime));
            requestDTO.setWateringFrequency(LocalDateTime.parse(wateringFrequency));
            requestDTO.setLightAccess(Light.valueOf(lightAccess));
            requestDTO.setLightScore(lightScore);

            PlantResponseDTO responseDTO = plantService.uploadImages(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
