package com.example.smartplantbuddy.validation;

import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.exception.plant.InvalidPlantException;
import com.example.smartplantbuddy.exception.plant.PlantDescriptionException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class PlantValidator {
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public PlantValidator(UserRepository userRepository, PlantRepository plantRepository) {
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    public void validatePlant(PlantRequestDTO plantRequestDTO) {
        if (plantRequestDTO.getName() == null || plantRequestDTO.getName().isEmpty()) {
            throw new InvalidPlantException("Plant name cannot be empty");
        }
        if (plantRequestDTO.getDescription() != null && plantRequestDTO.getDescription().length() > 100) {
            throw new PlantDescriptionException("Description of plant if exists should be shorter than 100 character length.");
        }
        // Rest are setting in mobile app
    }
    public Plant validateUpdatingPlant(Long plantId, PlantRequestDTO plantRequestDTO) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = "+ plantId));
        if (plantRequestDTO.getName() == null || plantRequestDTO.getName().isEmpty()) {
            throw new InvalidPlantException("Plant name cannot be empty");
        }
        if (plantRequestDTO.getDescription() != null && plantRequestDTO.getDescription().length() > 100) {
            throw new PlantDescriptionException("Description of plant if exists should be shorter than 100 character length.");
        }
        return plant;
    }
}
