package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.plant.PlantRequestDTO;
import com.example.smartplantbuddy.dto.plant.PlantResponseDTO;
import com.example.smartplantbuddy.mapper.CommentMapper;
import com.example.smartplantbuddy.mapper.NoteMapper;
import com.example.smartplantbuddy.mapper.PlantMapper;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.service.PlantService;
import com.example.smartplantbuddy.validation.PlantValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PlantServiceTest {

    @Mock
    private PlantValidator plantValidator;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private PlantMapper plantMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AmazonS3 s3client;

    @InjectMocks
    private PlantService plantService;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    private PlantRequestDTO requestDTO;
    private PlantResponseDTO responseDTO;
    private Plant plant;

    @BeforeEach
    public void setUp() {
        requestDTO = new PlantRequestDTO();
        responseDTO = new PlantResponseDTO();
        plant = new Plant();
    }

    @Test
    public void testUploadImages() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", "text/plain", "some xml".getBytes());
        requestDTO.setPlantImage(file);
        when(plantMapper.toEntity(requestDTO)).thenReturn(plant);
        when(plantRepository.save(any(Plant.class))).thenReturn(plant);
        when(plantMapper.toDTO(plant)).thenReturn(responseDTO);

        PlantResponseDTO result = plantService.uploadImages(requestDTO);

        assertNotNull(result);
        verify(plantValidator).validatePlant(requestDTO);
        verify(s3client).putObject(any(PutObjectRequest.class));
    }

    @Test
    public void testGetPlants() {
        when(plantRepository.findAll()).thenReturn(Arrays.asList(plant));
        when(plantMapper.toDTO(plant)).thenReturn(responseDTO);

        List<PlantResponseDTO> results = plantService.getPlants();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        verify(plantRepository).findAll();
    }

    @Test
    public void testUpdatePlant() {
        Long plantId = 1L;
        PlantRequestDTO updateRequestDTO = new PlantRequestDTO(); // set the necessary fields
        when(plantValidator.validateUpdatingPlant(plantId, updateRequestDTO)).thenReturn(plant);
        when(plantRepository.save(plant)).thenReturn(plant);
        when(plantMapper.toDTO(plant)).thenReturn(responseDTO);

        PlantResponseDTO updatedPlant = plantService.updatePlant(plantId, updateRequestDTO);

        assertNotNull(updatedPlant);
        verify(plantValidator).validateUpdatingPlant(plantId, updateRequestDTO);
        verify(plantRepository).save(plant);
    }
    @Test
    public void testDeletePlant() {
        Long plantId = 1L;
        when(plantRepository.existsById(plantId)).thenReturn(true);
        plantService.deletePlant(plantId);

        verify(plantRepository).existsById(plantId);
        verify(plantRepository).deleteById(plantId);

    }


}