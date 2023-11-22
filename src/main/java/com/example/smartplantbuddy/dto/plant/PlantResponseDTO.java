package com.example.smartplantbuddy.dto.plant;

import com.example.smartplantbuddy.model.enums.Light;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

public class PlantResponseDTO {
    private Long id;
    private String name;
    private String plantImageUrl;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringFrequency;
    private Light lightAccess;
    private int lightScore;
    private Set<Long> userIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlantImageUrl() {
        return plantImageUrl;
    }

    public void setPlantImageUrl(String plantImageUrl) {
        this.plantImageUrl = plantImageUrl;
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

    public String getLightAccess() {
        return lightAccess.toString();
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
    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }
}
