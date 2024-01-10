package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.exception.user.UsernameNotFoundException;
import com.example.smartplantbuddy.model.Invitation;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.InvitationRepository;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    public UserService(UserRepository userRepository, InvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
    }

    public Invitation sendInvitation(Long inviterId, Long inviteeId) {
        User inviter = userRepository.findById(inviterId).orElseThrow(() -> new RuntimeException("Inviter not found"));
        User invitee = userRepository.findById(inviteeId).orElseThrow(() -> new RuntimeException("Invitee not found"));

        Invitation invitation = new Invitation();
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setSentAt(LocalDateTime.now());
        invitation.setAccepted(false);

        return invitationRepository.save(invitation);
    }

    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setRespondedAt(LocalDateTime.now());
        invitation.setAccepted(true);

        User inviter = invitation.getInviter();
        User invitee = invitation.getInvitee();

        inviter.getFriends().add(invitee);
        invitee.getFriends().add(inviter);

        userRepository.save(inviter);
        userRepository.save(invitee);

        invitationRepository.save(invitation);
    }

    public void declineInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setRespondedAt(LocalDateTime.now());
        invitation.setAccepted(false);

        invitationRepository.save(invitation);
    }
    public Long findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId();
    }
}
