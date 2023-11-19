package com.example.smartplantbuddy.model;

import com.example.smartplantbuddy.controller.enums.Light;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity
@Data
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDateTime wateringTime;
    private LocalDateTime wateringFrequency;
    private Light lightAccess;
    private int lightScore;
}
