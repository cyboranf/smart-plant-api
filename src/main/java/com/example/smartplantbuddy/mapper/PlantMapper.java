package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class PlantMapper {
    public Plant toEntity(PlantRequestDTO dto) {
        Plant plant = new Plant();

        plant.setName(dto.getName());
//        plant.setImageUrl();  it will be setting in service method
        plant.setWateringTime(dto.getWateringTime());
        plant.setWateringFrequency(dto.getWateringFrequency());
        plant.setLightAccess(dto.getLightAccess());
        plant.setLightScore(dto.getLightScore());

        return plant;
    }

    public PlantResponseDTO toDTO(Plant plant) {
        PlantResponseDTO responseDTO = new PlantResponseDTO();

        responseDTO.setId(plant.getId());
        responseDTO.setName(plant.getName());
        responseDTO.setPlantImageUrl(plant.getImageUrl());
        responseDTO.setWateringTime(plant.getWateringTime());
        responseDTO.setWateringFrequency(plant.getWateringFrequency());
        responseDTO.setLightAccess(plant.getLightAccess());
        responseDTO.setLightScore(plant.getLightScore());
        if (plant.getUsers() != null) { // Check for null
            responseDTO.setUserIds(plant.getUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet()));
        } else {
            responseDTO.setUserIds(new HashSet<>()); // Set an empty set if users is null
        }

        return responseDTO;
    }
}
