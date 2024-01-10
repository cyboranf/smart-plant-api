package com.example.smartplantbuddy.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a comment made by a user on a friend's plant or note.
 * Comments are used to share thoughts, advice, or feedback on plant care.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "comments")
public class Comment {
    /**
     * The unique identifier for each comment.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The text content of the comment.
     */
    private String content;

    /**
     * The timestamp when the comment was created.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timestamp;

    /**
     * The user who made the comment.
     * This is a many-to-one relationship as a user can make many comments.
     */
    @ManyToOne
    private User user;

    /**
     * The plant on which the comment is made.
     * This is a many-to-one relationship as a plant can have many comments.
     */
    @ManyToOne
    private Plant plant;

    /**
     * The note on which the comment is made, if the comment is on a note.
     * This is an optional field and is null if the comment is directly on a plant.
     */
    @ManyToOne
    private Note note;
}
