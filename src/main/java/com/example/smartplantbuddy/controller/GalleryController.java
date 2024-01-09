package com.example.smartplantbuddy.controller;


import com.example.smartplantbuddy.dto.gallery.GalleryRequestDTO;
import com.example.smartplantbuddy.dto.gallery.GalleryResponseDTO;
import com.example.smartplantbuddy.service.GalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing gallery images associated with plants.
 * Provides endpoints for adding, deleting, and retrieving gallery images.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/gallery")
public class GalleryController {
    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    /**
     * Adds a new image to a plant's gallery.
     *
     * @param plantId The ID of the plant to which the image is to be added.
     * @param image The multipart file containing the image.
     * @return The response entity with the added gallery image details.
     * @throws IOException if an error occurs during file upload.
     */
    @PostMapping("/add")
    public ResponseEntity<GalleryResponseDTO> addImageToPlant(@RequestParam("plantId") Long plantId,
                                                              @RequestParam("image") MultipartFile image) throws IOException {
        GalleryRequestDTO galleryRequestDTO = new GalleryRequestDTO();
        galleryRequestDTO.setPlantImage(image);
        galleryRequestDTO.setPlantId(plantId);

        GalleryResponseDTO response = galleryService.addImageToPlant(galleryRequestDTO);
        return ResponseEntity.ok(response);
    }
    /**
     * Deletes a gallery image by its ID.
     *
     * @param galleryId The ID of the gallery image to be deleted.
     * @return The response entity indicating the result of the operation.
     */
    @DeleteMapping("/delete/{galleryId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long galleryId) {
        galleryService.deleteImage(galleryId);
        return ResponseEntity.ok().build();
    }
    /**
     * Retrieves all gallery images associated with a specific plant ID.
     *
     * @param plantId The ID of the plant whose gallery images are to be retrieved.
     * @return The response entity with a list of gallery images.
     */
    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<GalleryResponseDTO>> getGalleriesByPlantId(@PathVariable Long plantId) {
        List<GalleryResponseDTO> galleries = galleryService.getGalleriesByPlantId(plantId);
        return ResponseEntity.ok(galleries);
    }
}