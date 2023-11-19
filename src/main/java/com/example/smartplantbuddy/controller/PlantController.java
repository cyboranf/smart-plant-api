package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant")
public class PlantController {
    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Plant>> findAll() {
        List<Plant> plants = plantService.findAll();
        return ResponseEntity.ok(plants);
    }



}
