package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.gallery.GalleryRequestDTO;
import com.example.smartplantbuddy.dto.gallery.GalleryResponseDTO;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.model.Gallery;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

@Component
public class GalleryMapper {
    private final PlantRepository plantRepository;

    public GalleryMapper(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Gallery toEntity(GalleryRequestDTO galleryRequestDTO) {
        Gallery gallery = new Gallery();
        // TODO: set imageURL in service
//        gallery.setImageUrl(galleryRequestDTO.getPlantImage().getOriginalFilename());
        Plant plant = plantRepository.findById(galleryRequestDTO.getPlantId()).
                orElseThrow(() -> new PlantNotFoundException("Plant not found"));
        gallery.setPlant(plant);
        return gallery;
    }

    public GalleryResponseDTO toDTO(Gallery gallery) {
        GalleryResponseDTO galleryResponseDTO = new GalleryResponseDTO();
        galleryResponseDTO.setId(gallery.getId());
        galleryResponseDTO.setImageUrl(gallery.getImageUrl());
        galleryResponseDTO.setPlantId(gallery.getPlant().getId());
        return galleryResponseDTO;
    }
}
