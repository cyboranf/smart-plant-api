package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.gallery.GalleryRequestDTO;
import com.example.smartplantbuddy.dto.gallery.GalleryResponseDTO;
import com.example.smartplantbuddy.exception.gallery.ImageNotFoundException;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.mapper.GalleryMapper;
import com.example.smartplantbuddy.model.Gallery;
import com.example.smartplantbuddy.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final GalleryMapper galleryMapper;
    private final PlantService plantService;
    private final AmazonS3 s3client;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    public GalleryService(GalleryRepository galleryRepository, GalleryMapper galleryMapper, PlantService plantService, AmazonS3 s3client) {
        this.galleryRepository = galleryRepository;
        this.galleryMapper = galleryMapper;
        this.plantService = plantService;
        this.s3client = s3client;
    }

    public GalleryResponseDTO addImageToPlant(GalleryRequestDTO galleryRequestDTO) throws IOException {
        MultipartFile file = galleryRequestDTO.getPlantImage();
        if (file.isEmpty()) {
            throw new ImageEmptyException("You didn't send an image which you want to upload.");
        }

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String s3Path = uploadFileToS3(file, fileName, galleryRequestDTO.getPlantId());

        Gallery gallery = galleryMapper.toEntity(galleryRequestDTO);
        gallery.setImageUrl(s3Path);

        Gallery savedGallery = galleryRepository.save(gallery);
        return galleryMapper.toDTO(savedGallery);
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    private String uploadFileToS3(MultipartFile file, String fileName, Long plantId) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        String s3Path = "plant/" + plantId + "/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, tempFile));

        tempFile.deleteOnExit();

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Path;
    }
    public void deleteImage(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ImageNotFoundException("Gallery entry not found."));

        deleteFileFromS3(gallery.getImageUrl());
        galleryRepository.deleteById(galleryId);
    }

    private void deleteFileFromS3(String fileUrl) {
        String s3Key = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        s3client.deleteObject(bucketName, s3Key);
    }
    public List<GalleryResponseDTO> getGalleriesByPlantId(Long plantId) {
        List<Gallery> galleries = galleryRepository.findByPlantIdOrderByIdDesc(plantId);
        return galleries.stream()
                .map(galleryMapper::toDTO)
                .collect(Collectors.toList());
    }
}