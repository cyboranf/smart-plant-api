package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.invitation.InvitationRequestDTO;
import com.example.smartplantbuddy.dto.invitation.InvitationResponseDTO;
import com.example.smartplantbuddy.exception.invitation.InviteeNotFoundException;
import com.example.smartplantbuddy.exception.invitation.InviterNotFoundException;
import com.example.smartplantbuddy.model.Invitation;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InvitationMapper {
    private final UserRepository userRepository;

    public InvitationMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Invitation toEntity(InvitationRequestDTO invitationRequestDTO) {
        Invitation invitation = new Invitation();
        User inviter = userRepository.findById(invitationRequestDTO.getInviterId())
                .orElseThrow(() -> new InviterNotFoundException("Inviter not found with id = " + invitationRequestDTO.getInviterId()));
        User invitee = userRepository.findById(invitationRequestDTO.getInviteeId())
                .orElseThrow(() -> new InviteeNotFoundException("Invitee not found with id = " + invitationRequestDTO.getInviteeId()));
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setSentAt(LocalDateTime.now());
        invitation.setAccepted(false);
        return invitation;
    }

    public InvitationResponseDTO toDTO(Invitation invitation) {
        InvitationResponseDTO invitationResponseDTO = new InvitationResponseDTO();
        invitationResponseDTO.setId(invitation.getId());
        invitationResponseDTO.setInviterId(invitation.getInviter().getId());
        invitationResponseDTO.setInviteeId(invitation.getInvitee().getId());
        invitationResponseDTO.setSentAt(invitation.getSentAt());
        invitationResponseDTO.setRespondedAt(invitation.getRespondedAt());
        invitationResponseDTO.setAccepted(invitation.isAccepted());
        return invitationResponseDTO;
    }
}
