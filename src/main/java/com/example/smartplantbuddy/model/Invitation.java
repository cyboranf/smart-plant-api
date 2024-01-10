package com.example.smartplantbuddy.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a friend invitation between users.
 * It tracks the inviter, invitee, and the status of the invitation.
 */
@Entity
@Data
@Table(name = "invitations")
public class Invitation {
    /**
     * The unique identifier for each invitation.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The user who sent the invitation.
     */
    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private User inviter;

    /**
     * The user who received the invitation.
     */
    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private User invitee;
    /**
     * The date and time the invitation was sent.
     */
    private LocalDateTime sentAt;
    /**
     * The date and time the invitation was responded to.
     */
    private LocalDateTime respondedAt;

    private boolean accepted; //flag to indicate if the invitation was accepted or not
}
