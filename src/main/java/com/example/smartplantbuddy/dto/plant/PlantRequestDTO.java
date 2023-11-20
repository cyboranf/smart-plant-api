package com.example.smartplantbuddy.dto.plant;

import com.example.smartplantbuddy.model.enums.Light;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class PlantRequestDTO {
    private String name;
    private MultipartFile plantImage;
    private LocalDateTime wateringTime;
    private LocalDateTime wateringFrequency;
    private Light lightAccess;
    private int lightScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(MultipartFile plantImage) {
        this.plantImage = plantImage;
    }

    public LocalDateTime getWateringTime() {
        return wateringTime;
    }

    public void setWateringTime(LocalDateTime wateringTime) {
        this.wateringTime = wateringTime;
    }

    public LocalDateTime getWateringFrequency() {
        return wateringFrequency;
    }

    public void setWateringFrequency(LocalDateTime wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }

    public Light getLightAccess() {
        return lightAccess;
    }

    public void setLightAccess(Light lightAccess) {
        this.lightAccess = lightAccess;
    }

    public int getLightScore() {
        return lightScore;
    }

    public void setLightScore(int lightScore) {
        this.lightScore = lightScore;
    }
}
