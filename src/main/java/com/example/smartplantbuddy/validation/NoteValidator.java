package com.example.smartplantbuddy.validation;

import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.exception.note.InvalidNoteException;
import com.example.smartplantbuddy.exception.note.NoteNotFoundException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.model.Note;
import com.example.smartplantbuddy.repository.NoteRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.stereotype.Component;

@Component
public class NoteValidator {
    private final PlantRepository plantRepository;
    private final NoteRepository noteRepository;

    public NoteValidator(PlantRepository plantRepository, NoteRepository noteRepository) {
        this.plantRepository = plantRepository;
        this.noteRepository = noteRepository;
    }

    public void noteRequestDTOValidation(NoteRequestDTO requestDTO) {
        if (requestDTO.getNote() == null || requestDTO.getNote().length() > 100) {
            throw new InvalidNoteException("Note length should be shorter than 100 characters.");
        }
        plantRepository.findById(requestDTO.getPlantId())
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = " + requestDTO.getPlantId() + " to add note."));
    }

    public void plantToAddNoteValidation(Long plantId) {
        plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = " + plantId + " to add note to them."));
    }


    public Note noteUpdateValidation(Long noteId, NoteRequestDTO requestDTO) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Can not found note with id = " + noteId));
        if (requestDTO.getNote() == null || requestDTO.getNote().length() > 100) {

            throw new InvalidNoteException("Note length should be shorter than 100 characters.");
        }
        plantRepository.findById(requestDTO.getPlantId())
                .orElseThrow(() -> new PlantNotFoundException("Can not found Plant with id = " + requestDTO.getPlantId() + " to add note."));
        return note;
    }

    public Note removingNoteValidation(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Can not found note with id = " + noteId));
        return note;
    }
}
