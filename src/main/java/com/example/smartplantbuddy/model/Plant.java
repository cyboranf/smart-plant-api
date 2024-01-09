package com.example.smartplantbuddy.model;

import com.example.smartplantbuddy.model.enums.Light;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a plant in the application.
 * The Plant entity contains information such as the plant's name, image, care details, and associated notes and galleries.
 * It also tracks the watering and fertilizing schedules for the plant.
 * The entity is linked with users to potentially represent ownership or care responsibilities.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table
public class Plant {
    /**
     * The unique identifier for each plant.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the plant.
     */
    private String name;

    /**
     * The URL of the plant's image.
     * This URL points to the location where the plant's image is stored.
     */
    private String imageUrl;

    /**
     * A description of the plant.
     * This text provides additional information about the plant.
     */
    private String description;

    /**
     * The last scheduled watering time for the plant.
     * It includes both date and time components.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringTime;

    /**
     * The frequency of watering the plant, measured in hours.
     */
    private int wateringFrequency;

    /**
     * The light access category for the plant, which indicates its light requirements.
     */
    private Light lightAccess;

    /**
     * A score indicating the plant's overall light condition.
     */
    private int lightScore;

    /**
     * A set of users associated with the plant.
     * This could represent the users who are responsible for caring for the plant.
     */
    @ManyToMany(mappedBy = "plants")
    private Set<User> users = new HashSet<>();

    /**
     * A list of notes associated with the plant.
     * This provides a history of observations and reminders about the plant's care.
     */
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Note> notes;

    /**
     * The frequency of fertilizing the plant, measured in hours.
     */
    private int fertilizingFrequency;

    /**
     * The next scheduled fertilizing time for the plant.
     * It includes both date and time components.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fertilizingTime;

    /**
     * A list of gallery images associated with the plant.
     * This could include various images of the plant at different stages of growth or during different seasons.
     */
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Gallery> galleries;
}
