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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private InvitationMapper invitationMapper;

    @InjectMocks
    private UserService userService;

    private User inviter;
    private User invitee;
    private Invitation invitation;
    private UserResponseDTO userResponseDTO;
    private InvitationResponseDTO invitationResponseDTO;

    @BeforeEach
    public void setUp() {
        inviter = new User();
        inviter.setId(1L);
        inviter.setFriends(new HashSet<>());

        invitee = new User();
        invitee.setId(2L);
        invitee.setFriends(new HashSet<>());

        invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setSentAt(LocalDateTime.now());
        invitation.setAccepted(false);

        userResponseDTO = new UserResponseDTO();
        invitationResponseDTO = new InvitationResponseDTO();
    }

    @Test
    public void testSendInvitation() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(inviter));
        when(userRepository.findById(2L)).thenReturn(Optional.of(invitee));
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);
        when(invitationMapper.toDTO(invitation)).thenReturn(invitationResponseDTO);

        InvitationResponseDTO result = userService.sendInvitation(1L, 2L);

        assertNotNull(result);
        verify(userRepository, times(2)).findById(anyLong());
        verify(invitationRepository).save(any(Invitation.class));
        verify(invitationMapper).toDTO(any(Invitation.class));
    }

    @Test
    public void testAcceptInvitation() {
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitation));
        when(userRepository.save(any(User.class))).thenReturn(inviter);
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);

        userService.acceptInvitation(1L);

        assertTrue(invitation.isAccepted());
        verify(invitationRepository).findById(anyLong());
        verify(userRepository, times(2)).save(any(User.class));
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    public void testDeclineInvitation() {
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);

        userService.declineInvitation(1L);

        assertFalse(invitation.isAccepted());
        verify(invitationRepository).findById(anyLong());
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    public void testFindByLogin() {
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(inviter));

        Long userId = userService.findByLogin("testuser");

        assertNotNull(userId);
        assertEquals(1L, userId);
        verify(userRepository).findByLogin("testuser");
    }
}
