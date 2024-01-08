package com.example.smartplantbuddy.controller;


import com.example.smartplantbuddy.dto.gallery.GalleryRequestDTO;
import com.example.smartplantbuddy.dto.gallery.GalleryResponseDTO;
import com.example.smartplantbuddy.service.GalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {
    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping("/add")
    public ResponseEntity<GalleryResponseDTO> addImageToPlant(@RequestParam("plantId") Long plantId,
                                                              @RequestParam("image") MultipartFile image) throws IOException {
        GalleryRequestDTO galleryRequestDTO = new GalleryRequestDTO();
        galleryRequestDTO.setPlantImage(image);
        galleryRequestDTO.setPlantId(plantId);

        GalleryResponseDTO response = galleryService.addImageToPlant(galleryRequestDTO);
        return ResponseEntity.ok(response);
    }
}