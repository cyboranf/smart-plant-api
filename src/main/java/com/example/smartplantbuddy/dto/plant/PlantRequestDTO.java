package com.example.smartplantbuddy.dto.plant;

import com.example.smartplantbuddy.model.enums.Light;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class PlantRequestDTO {
    private String name;
    private MultipartFile plantImage;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringTime;
    private int wateringFrequency; // bcs quantity of hours
    private Light lightAccess;
    private int lightScore;
    private int fertilizingFrequency; // in hours
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fertilizingTime;
}
