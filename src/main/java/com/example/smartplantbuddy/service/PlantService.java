package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.mapper.PlantMapper;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    private final AmazonS3 s3client;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    public PlantService(PlantRepository plantRepository, PlantMapper plantMapper, AmazonS3 s3client) {
        this.plantRepository = plantRepository;
        this.plantMapper = plantMapper;
        this.s3client = s3client;
    }

    public PlantResponseDTO uploadImages(PlantRequestDTO requestDTO) throws IOException {
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

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        String s3Path = "plants/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, tempFile));

        tempFile.deleteOnExit();

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Path;
    }
}
