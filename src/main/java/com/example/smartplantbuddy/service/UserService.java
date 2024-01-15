package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.dto.invitation.InvitationResponseDTO;
import com.example.smartplantbuddy.dto.user.UserResponseDTO;
import com.example.smartplantbuddy.exception.invitation.InvitationNotFoundException;
import com.example.smartplantbuddy.exception.invitation.InviteeNotFoundException;
import com.example.smartplantbuddy.exception.invitation.InviterNotFoundException;
import com.example.smartplantbuddy.exception.user.UserNotFoundException;
import com.example.smartplantbuddy.exception.user.UsernameNotFoundException;
import com.example.smartplantbuddy.mapper.InvitationMapper;
import com.example.smartplantbuddy.mapper.UserMapper;
import com.example.smartplantbuddy.model.Invitation;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.InvitationRepository;
import com.example.smartplantbuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final UserMapper userMapper;
    private final InvitationMapper invitationMapper;

    public UserService(UserRepository userRepository, InvitationRepository invitationRepository, UserMapper userMapper, InvitationMapper invitationMapper) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.userMapper = userMapper;
        this.invitationMapper = invitationMapper;
    }

    public InvitationResponseDTO sendInvitation(Long inviterId, Long inviteeId) {
        User inviter = userRepository.findById(inviterId).orElseThrow(() -> new InviterNotFoundException("Inviter not found"));
        User invitee = userRepository.findById(inviteeId).orElseThrow(() -> new InviteeNotFoundException("Invitee not found"));

        Invitation invitation = new Invitation();
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setSentAt(LocalDateTime.now());
        invitation.setAccepted(false);

        Invitation savedInvitation = invitationRepository.save(invitation);

        return invitationMapper.toDTO(savedInvitation);
    }

    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        if (!invitation.isAccepted()) {
            invitation.setRespondedAt(LocalDateTime.now());
            invitation.setAccepted(true);

            User inviter = invitation.getInviter();
            User invitee = invitation.getInvitee();

            inviter.getFriends().add(invitee);
            invitee.getFriends().add(inviter);

            userRepository.save(inviter);
            userRepository.save(invitee);

            invitationRepository.save(invitation);
        } else {
            throw new IllegalStateException("Invitation already accepted");
        }
    }

    public void declineInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));
        invitation.setRespondedAt(LocalDateTime.now());
        invitation.setAccepted(false);

        invitationRepository.save(invitation);
    }

    public Long findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId();
    }

    public List<UserResponseDTO> getUserFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return user.getFriends().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<InvitationResponseDTO> getUserInvitations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return user.getReceivedInvitations().stream()
                .map(invitationMapper::toDTO)
                .collect(Collectors.toList());
    }


}
