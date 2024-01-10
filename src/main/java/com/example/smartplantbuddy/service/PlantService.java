package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.mapper.PlantMapper;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import com.example.smartplantbuddy.validation.PlantValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer for managing plant-related data.
 * This service provides functionality to add, update, delete, and retrieve plant information,
 * including the handling of plant images via Amazon S3 storage service.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
public class PlantService {
    private final PlantValidator plantValidator;
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    private final AmazonS3 s3client;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    public PlantService(PlantValidator plantValidator, PlantRepository plantRepository, PlantMapper plantMapper, AmazonS3 s3client) {
        this.plantValidator = plantValidator;
        this.plantRepository = plantRepository;
        this.plantMapper = plantMapper;
        this.s3client = s3client;
    }

    /**
     * Uploads a plant image to Amazon S3 and saves the plant information in the database.
     *
     * @param requestDTO The DTO containing the plant details and the image to be uploaded.
     * @return The response DTO containing details of the saved plant including the image URL.
     * @throws IOException if an I/O error occurs during file upload.
     */
    public PlantResponseDTO uploadImages(PlantRequestDTO requestDTO) throws IOException {
        plantValidator.validatePlant(requestDTO);
        MultipartFile file = requestDTO.getPlantImage();
        if (file.isEmpty()) {
            throw new ImageEmptyException("You didn't send an image which You want to upload.");
        }

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String s3Path = uploadFileToS3(file, fileName);

        Plant plant = plantMapper.toEntity(requestDTO);
        plant.setImageUrl(s3Path);

        Plant savedPlant = plantRepository.save(plant);

        return plantMapper.toDTO(savedPlant);
    }

    /**
     * Generates a unique file name for the uploaded image.
     * This method prepends a random UUID to the original file name to ensure uniqueness.
     *
     * @param originalFilename The original file name of the uploaded image.
     * @return The unique file name.
     */
    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    /**
     * Uploads a file to Amazon S3.
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        String s3Path = "plants/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, tempFile));

        tempFile.deleteOnExit();

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Path;
    }

    /**
     * Retrieves a list of all plants in the database, converting them to their respective DTOs.
     *
     * @return A list of DTOs representing all plants.
     */
    public List<PlantResponseDTO> getPlants() {
        return plantRepository.findAll().stream()
                .map(plantMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the details of an existing plant by its ID.
     * If the plant is not found, a PlantNotFoundException is thrown.
     *
     * @param plantId    The ID of the plant to update.
     * @param requestDTO The DTO containing the updated plant details.
     * @return The response DTO containing details of the updated plant.
     */
    public PlantResponseDTO updatePlant(Long plantId, PlantRequestDTO requestDTO) {
        Plant plant = plantValidator.validateUpdatingPlant(plantId, requestDTO);

        plant.setName(requestDTO.getName());
        plant.setDescription(requestDTO.getDescription());
        plant.setWateringTime(requestDTO.getWateringTime());
        plant.setWateringFrequency(requestDTO.getWateringFrequency());
        plant.setLightAccess(requestDTO.getLightAccess());
        plant.setLightScore(requestDTO.getLightScore());
        plant.setFertilizingFrequency(requestDTO.getFertilizingFrequency());
        plant.setFertilizingTime(requestDTO.getFertilizingTime());

        Plant updatedPlant = plantRepository.save(plant);
        return plantMapper.toDTO(updatedPlant);
    }

    /**
     * Deletes a plant from the database by its ID.
     * If the plant does not exist, a PlantNotFoundException is thrown.
     *
     * @param plantId The ID of the plant to be deleted.
     */
    public void deletePlant(Long plantId) {
        if (!plantRepository.existsById(plantId)) {
            throw new PlantNotFoundException("Plant with id " + plantId + " not found");
        }
        plantRepository.deleteById(plantId);
    }

    /**
     * Updates the watering and fertilizing times for an existing plant by its ID.
     * This method allows partial updates to the plant's care schedule.
     *
     * @param plantId         The ID of the plant to update.
     * @param wateringTime    The new watering time for the plant.
     * @param fertilizingTime The new fertilizing time for the plant.
     * @return The response DTO containing details of the updated plant.
     */
    public PlantResponseDTO updatePlantTimes(Long plantId, LocalDateTime wateringTime, LocalDateTime fertilizingTime) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException("Plant with id " + plantId + " not found"));

        plant.setWateringTime(wateringTime);
        plant.setFertilizingTime(fertilizingTime);

        Plant updatedPlant = plantRepository.save(plant);
        return plantMapper.toDTO(updatedPlant);
    }
}
