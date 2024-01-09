package com.example.smartplantbuddy.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Represents an image gallery associated with a plant.
 * This entity links images to the plant entity showing different views or stages of the plant.
 * Each gallery object can have multiple images but is associated with one single plant.
 * The gallery entity contains an auto-generated unique identifier for each image, the URL where the image is stored,
 * and the plant associated with the image.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table
@Data
public class Gallery {

    /**
     * The unique identifier for each gallery image.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The URL of the image.
     * This is a string value which holds the location where the image is stored (AWS S3).
     */
    private String imageUrl;

    /**
     * The plant associated with the gallery image.
     * This is a many-to-one relationship as many gallery images can be associated with one plant.
     * It is lazily fetched which means it's fetched on demand and not immediately with the gallery entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private Plant plant;
}
