package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.mapper.PlantMapper;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    public PlantService(PlantRepository plantRepository, PlantMapper plantMapper) {
        this.plantRepository = plantRepository;
        this.plantMapper = plantMapper;
    }

    public List<Plant> findAll() {
        return plantRepository.findAll();
    }


}
