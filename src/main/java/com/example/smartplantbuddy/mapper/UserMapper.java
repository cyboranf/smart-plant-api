package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.user.UserRequestDTO;
import com.example.smartplantbuddy.dto.user.UserResponseDTO;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.RoleRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between User entity and DTO objects.
 */
@Component
public class UserMapper {
    private final RoleRepository roleRepository;
    private final PlantRepository plantRepository;

    public UserMapper(RoleRepository roleRepository, PlantRepository plantRepository) {
        this.roleRepository = roleRepository;
        this.plantRepository = plantRepository;
    }

    /**
     * Converts a UserRequestDTO to a User entity.
     *
     * @param dto The UserRequestDTO to convert.
     * @return The converted User entity.
     */
    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setRoles(new HashSet<>(roleRepository.findAllById(dto.getRoleIds())));
        user.setPlants(new HashSet<>(plantRepository.findAllById(dto.getPlantIds())));
        return user;
    }


    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
        dto.setPlantIds(user.getPlants().stream().map(plant -> plant.getId()).collect(Collectors.toSet()));
        dto.setReceivedInvitationIds(user.getReceivedInvitations().stream().map(invitation -> invitation.getId()).collect(Collectors.toSet()));
        dto.setSentInvitationIds(user.getSentInvitations().stream().map(invitation -> invitation.getId()).collect(Collectors.toSet()));
        return dto;
    }
}
