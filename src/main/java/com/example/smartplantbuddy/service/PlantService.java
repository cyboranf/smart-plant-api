package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.mapper.PlantMapper;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;

    private final String imageDirectory = "uploaded-images/";

    public PlantService(PlantRepository plantRepository, PlantMapper plantMapper) {
        this.plantRepository = plantRepository;
        this.plantMapper = plantMapper;
    }

    public PlantResponseDTO uploadImages(PlantRequestDTO requestDTO) throws IOException {
        MultipartFile file = requestDTO.getPlantImage();

        if (file.isEmpty()) {
            throw new ImageEmptyException("You didn't send image which You want to upload.");
        }

        String fileName = generateUniqueFileName(file.getName());
        saveFile(file, fileName);

        Plant plant = plantMapper.toEntity(requestDTO);
        plant.setImageUrl(imageDirectory + fileName);

        Plant savedPlant = plantRepository.save(plant);
        return plantMapper.toDTO(savedPlant);
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    private void saveFile(MultipartFile file, String fileName) throws IOException {
        Path destinationFilePath = Paths.get(imageDirectory + fileName);

        // Ensure the directory exists
        Files.createDirectories(destinationFilePath.getParent());

        // Save the file
        Files.copy(file.getInputStream(), destinationFilePath);
    }
}
