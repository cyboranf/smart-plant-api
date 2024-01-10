package com.example.smartplantbuddy.validation;

import com.example.smartplantbuddy.dto.gallery.GalleryRequestDTO;
import com.example.smartplantbuddy.exception.gallery.ImageNotFoundException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.model.Gallery;
import com.example.smartplantbuddy.repository.GalleryRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

@Component
public class GalleryValidator {
    private final PlantRepository plantRepository;
    private final GalleryRepository galleryRepository;

    public GalleryValidator(PlantRepository plantRepository, GalleryRepository galleryRepository) {
        this.plantRepository = plantRepository;
        this.galleryRepository = galleryRepository;
    }

    public void plantForGalleryValidation(GalleryRequestDTO galleryRequestDTO) {
        plantRepository.findById(galleryRequestDTO.getPlantId())
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = " + galleryRequestDTO.getPlantId() + " to add image to gallery."));
    }

    public void plantForGalleryValidationByID(Long id) {
        plantRepository.findById(id)
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = " + id + " to add image to gallery."));
    }

    public Gallery deleteImageOfPlantValidation(Long galleryId) {
        return galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ImageNotFoundException("Can not found image of plant with id = " + galleryId + " required to delete an image from s3 and from database"));

    }
}
