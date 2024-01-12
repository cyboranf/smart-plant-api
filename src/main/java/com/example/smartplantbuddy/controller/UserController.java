package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.invitation.InvitationResponseDTO;
import com.example.smartplantbuddy.dto.user.UserResponseDTO;
import com.example.smartplantbuddy.exception.user.UsernameNotFoundException;
import com.example.smartplantbuddy.model.Invitation;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.model.Role;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.security.JwtTokenProvider;
import com.example.smartplantbuddy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * REST controller for managing user data.
 * Provides an endpoint to check if a user exists based on the authentication context.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
    }
    // TODO: Invitation feature must be tested and checked.
    // TODO: Code in Plant Controller showing plants of other friends, code endpoint to see suggests friends
    /**
     * Checks if the current authenticated user exists in the system.
     *
     * @return A ResponseEntity with the user's details if found, or throws an exception if not found.
     */
    @GetMapping("/isUser")
    public ResponseEntity<?> isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with given username not found"));
        return ResponseEntity.ok(mapToUserResponseDTO(user));
    }

    /**
     * Sends a friend invitation to another user.
     *
     * @param inviteeId The ID of the user to whom the invitation is sent.
     * @return A ResponseEntity with the details of the sent invitation.
     */
    @PostMapping("/send-invitation")
    public ResponseEntity<InvitationResponseDTO> sendInvitation(@RequestParam Long inviteeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Long inviterId = userService.findByLogin(username);

        InvitationResponseDTO invitation = userService.sendInvitation(inviterId, inviteeId);
        return ResponseEntity.ok(invitation);
    }

    /**
     * Accepts a received friend invitation.
     *
     * @param invitationId The ID of the invitation to accept.
     * @return A ResponseEntity indicating the acceptance of the invitation.
     */
    @PutMapping("/accept-invitation/{invitationId}")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId) {
        userService.acceptInvitation(invitationId);
        return ResponseEntity.ok("Invitation accepted.");
    }
    /**
     * Declines a received friend invitation.
     *
     * @param invitationId The ID of the invitation to decline.
     * @return A ResponseEntity indicating the decline of the invitation.
     */
    @PutMapping("/decline-invitation/{invitationId}")
    public ResponseEntity<?> declineInvitation(@PathVariable Long invitationId) {
        userService.declineInvitation(invitationId);
        return ResponseEntity.ok("Invitation declined.");
    }

    /**
     * Maps a User entity to a UserResponseDTO.
     *
     * @param user The User entity to map.
     * @return A UserResponseDTO representing the user.
     */
    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setLogin(user.getLogin());
        userResponseDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        userResponseDTO.setPlantIds(user.getPlants().stream().map(Plant::getId).collect(Collectors.toSet()));
        return userResponseDTO;
    }
}
