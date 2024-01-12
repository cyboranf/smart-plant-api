package com.example.smartplantbuddy.dto.invitation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationResponseDTO {
    private Long id;
    private Long inviterId;
    private Long inviteeId;
    private LocalDateTime sentAt;
    private LocalDateTime respondedAt;
    private boolean accepted;
}
