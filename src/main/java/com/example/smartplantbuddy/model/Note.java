package com.example.smartplantbuddy.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a note for a plant within the application.
 * A note is a textual observation or reminder related to a plant's care or status and may optionally include an image.
 * Each note records the date and time when the observation was made or when the reminder is due.
 * Notes are linked to a specific plant and can be used to track plant care history and other details.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "notes")
public class Note {
    /**
     * The unique identifier for each note.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date of the note.
     * It is formatted to represent only the date component (yyyy-MM-dd).
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * The time of the note.
     * It is formatted to represent only the time component.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;

    /**
     * The text content of the note.
     * This text contains the user's observations or reminders about the plant.
     */
    private String note;

    /**
     * The URL of the image associated with the note, if any.
     * This URL points to the location where the image is stored (AWS S3).
     */
    private String imageUrl;

    /**
     * The plant associated with this note.
     * This is a many-to-one relationship as many notes can be associated with one plant.
     */
    @ManyToOne
    private Plant plant;
}
