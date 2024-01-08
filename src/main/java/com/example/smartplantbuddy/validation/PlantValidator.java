package com.example.smartplantbuddy.validation;

import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.exception.plant.InvalidPlantException;
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
    }
}
