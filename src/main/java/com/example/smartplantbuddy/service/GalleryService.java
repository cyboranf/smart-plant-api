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
/**
 * Provides services for managing the gallery images associated with plants.
 * This service includes adding new images to a plant's gallery, deleting images, and retrieving galleries by plant ID.
 * Images are uploaded to Amazon S3, and this service handles the upload and deletion of image files.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
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
    /**
     * Adds an image to the gallery for the given plant.
     * The image file is uploaded to Amazon S3 and then saved to the gallery.
     *
     * @param galleryRequestDTO The DTO containing the plant ID and the image file to be uploaded.
     * @return The response DTO containing details of the saved gallery image.
     * @throws IOException if an I/O error occurs during file upload.
     */
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
    /**
     * Generates a unique file name for an image to prevent collisions in the storage bucket.
     *
     * @param originalFilename The original filename of the uploaded file.
     * @return A unique file name.
     */
    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
    /**
     * Uploads a file to Amazon S3 in the bucket configured for the application.
     *
     * @param file The multipart file to be uploaded.
     * @param fileName The name of the file to be used in S3.
     * @param plantId The ID of the plant associated with this image.
     * @return The URL of the uploaded file.
     * @throws IOException if an I/O error occurs during file upload.
     */
    private String uploadFileToS3(MultipartFile file, String fileName, Long plantId) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        String s3Path = "plant/" + plantId + "/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, tempFile));

        tempFile.deleteOnExit();

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Path;
    }
    /**
     * Deletes an image from the gallery by its ID.
     * If the image exists in Amazon S3, it is also deleted from the bucket.
     *
     * @param galleryId The ID of the gallery entry to be deleted.
     */
    public void deleteImage(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ImageNotFoundException("Gallery entry not found."));

        deleteFileFromS3(gallery.getImageUrl());
        galleryRepository.deleteById(galleryId);
    }
    /**
     * Deletes a file from Amazon S3 based on the URL of the file.
     *
     * @param fileUrl The full URL of the file to be deleted.
     */
    private void deleteFileFromS3(String fileUrl) {
        String s3Key = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        s3client.deleteObject(bucketName, s3Key);
    }
    /**
     * Retrieves a list of gallery images associated with a given plant ID.
     * Images are returned in descending order by their ID, which typically represents the order of addition.
     *
     * @param plantId The ID of the plant whose gallery images are to be retrieved.
     * @return A list of gallery response DTOs containing the details of each image.
     */
    public List<GalleryResponseDTO> getGalleriesByPlantId(Long plantId) {
        List<Gallery> galleries = galleryRepository.findByPlantIdOrderByIdDesc(plantId);
        return galleries.stream()
                .map(galleryMapper::toDTO)
                .collect(Collectors.toList());
    }
}