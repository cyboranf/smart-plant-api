package com.example.smartplantbuddy.dto.plant;

import com.example.smartplantbuddy.model.enums.Light;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PlantResponseDTO {
    private Long id;
    private String name;
    private String plantImageUrl;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringTime;
    private int wateringFrequency; // bcs quantity of hours
    private Light lightAccess;
    private int lightScore;
    private Set<Long> userIds;
    private int fertilizingFrequency; // in hours
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fertilizingTime;
}