package com.example.smartplantbuddy.mapper;

import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.model.Note;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    private final PlantRepository plantRepository;

    public NoteMapper(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Note toEntity(NoteRequestDTO noteRequestDTO) {
        Note note = new Note();
        note.setDate(noteRequestDTO.getDate());
        note.setTime(noteRequestDTO.getTime());
        note.setNote(noteRequestDTO.getNote());
//        note.setImageUrl();
        Plant plant = plantRepository.findById(noteRequestDTO.getPlantId())
                        .orElseThrow(() -> new PlantNotFoundException("Can not found plant with id = "+ noteRequestDTO.getPlantId()));
        note.setPlant(plant);
        return note;
    }

    public NoteResponseDTO toDTO(Note note) {
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setId(note.getId());
        noteResponseDTO.setDate(note.getDate());
        noteResponseDTO.setTime(note.getTime());
        noteResponseDTO.setNote(note.getNote());
        noteResponseDTO.setPlantId(note.getPlant().getId());
        noteResponseDTO.setPlantImageUrl(note.getPlant().getImageUrl());
        return noteResponseDTO;
    }

}
