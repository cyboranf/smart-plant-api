package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.smartplantbuddy.dto.gallery.GalleryRequestDTO;
import com.example.smartplantbuddy.dto.gallery.GalleryResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.mapper.GalleryMapper;
import com.example.smartplantbuddy.model.Gallery;
import com.example.smartplantbuddy.repository.GalleryRepository;
import com.example.smartplantbuddy.validation.GalleryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GalleryServiceTest {

    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private GalleryMapper galleryMapper;

    @Mock
    private GalleryValidator galleryValidator;

    @Mock
    private AmazonS3 s3client;

    @InjectMocks
    private GalleryService galleryService;

    private static final String BUCKET_NAME = "test-bucket";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(galleryService, "bucketName", BUCKET_NAME);
    }

    @Test
    void addImageToPlant() throws IOException {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        GalleryRequestDTO galleryRequestDTO = new GalleryRequestDTO();
        galleryRequestDTO.setPlantImage(file);

        Gallery gallery = new Gallery();
        gallery.setImageUrl("test-url");

        GalleryResponseDTO expectedResponse = new GalleryResponseDTO();
        expectedResponse.setImageUrl("test-url"); // Assuming GalleryResponseDTO has this field

        when(galleryMapper.toEntity(any(GalleryRequestDTO.class))).thenReturn(gallery);
        when(galleryRepository.save(any(Gallery.class))).thenReturn(gallery);
        when(s3client.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());
        when(galleryMapper.toDTO(any(Gallery.class))).thenReturn(expectedResponse);


        GalleryResponseDTO result = galleryService.addImageToPlant(galleryRequestDTO);

        assertNotNull(result);
        assertEquals(expectedResponse.getImageUrl(), result.getImageUrl()); // Verify if the response is as expected
        verify(galleryRepository).save(any(Gallery.class)); // Verify galleryRepository.save() was called
        verify(s3client).putObject(any(PutObjectRequest.class)); // Verify s3client.putObject() was called
    }

    @Test
    void deleteImage() {
        Long galleryId = 1L;
        String imageUrl = "https://test-bucket.s3.amazonaws.com/plant/1/test.jpg";
        Gallery gallery = new Gallery();
        gallery.setId(galleryId);
        gallery.setImageUrl(imageUrl);

        when(galleryValidator.deleteImageOfPlantValidation(galleryId)).thenReturn(gallery);

        galleryService.deleteImage(galleryId);

        verify(galleryRepository).deleteById(galleryId);

        String expectedS3Key = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        verify(s3client).deleteObject(BUCKET_NAME, expectedS3Key);
    }


    @Test
    void getGalleriesByPlantId() {
        Long plantId = 1L;
        List<Gallery> galleries = List.of(new Gallery()); // Assuming Gallery has a no-arg constructor
        when(galleryRepository.findByPlantIdOrderByIdDesc(plantId)).thenReturn(galleries);
        when(galleryMapper.toDTO(any(Gallery.class))).thenReturn(new GalleryResponseDTO());    // Act
        List<GalleryResponseDTO> result = galleryService.getGalleriesByPlantId(plantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(galleryRepository).findByPlantIdOrderByIdDesc(plantId);
        verify(galleryMapper, times(1)).toDTO(any(Gallery.class));
    }

    @Test
    void addImageToPlant_ThrowsImageEmptyException() {
        GalleryRequestDTO galleryRequestDTO = new GalleryRequestDTO();
        galleryRequestDTO.setPlantImage(new MockMultipartFile("image", new byte[0]));

        assertThrows(ImageEmptyException.class, () -> {
            galleryService.addImageToPlant(galleryRequestDTO);
        });
    }
}
