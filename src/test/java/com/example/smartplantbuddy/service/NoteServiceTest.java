package com.example.smartplantbuddy.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.exception.note.NoteNotFoundException;
import com.example.smartplantbuddy.mapper.NoteMapper;
import com.example.smartplantbuddy.model.Note;
import com.example.smartplantbuddy.repository.NoteRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import com.example.smartplantbuddy.validation.NoteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteValidator noteValidator;

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private AmazonS3 s3client;

    @InjectMocks
    private NoteService noteService;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    private NoteRequestDTO requestDTO;
    private NoteResponseDTO responseDTO;
    private Note note;

    @BeforeEach
    public void setUp() {
        requestDTO = new NoteRequestDTO();
        responseDTO = new NoteResponseDTO();
        note = new Note();
        noteService = new NoteService(noteRepository, noteValidator, noteMapper, plantRepository, s3client);
        ReflectionTestUtils.setField(noteService, "bucketName", "smart-plant");
    }
    @Test
    public void testUpdateNote() throws IOException {
        Long noteId = 1L;
        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setImageUrl("existingImageUrl");

        MockMultipartFile newFile = new MockMultipartFile("newFile", "newFilename.jpg", "text/plain", "new xml".getBytes());
        requestDTO.setPlantImage(newFile);
        requestDTO.setNote("Updated Note");

        when(noteValidator.noteUpdateValidation(noteId, requestDTO)).thenReturn(existingNote);
        when(noteRepository.save(any(Note.class))).thenReturn(existingNote);
        when(noteMapper.toDTO(existingNote)).thenReturn(responseDTO);

        NoteResponseDTO updatedNoteResponse = noteService.updateNote(noteId, requestDTO);

        assertNotNull(updatedNoteResponse);
        verify(noteValidator).noteUpdateValidation(noteId, requestDTO);
        verify(s3client).deleteObject(bucketName, "existingImageUrl");
        verify(s3client).putObject(any(PutObjectRequest.class));
        verify(noteRepository).save(any(Note.class));
    }
    @Test
    public void testDeleteNote() {
        Long noteId = 1L;
        Note noteToDelete = new Note();
        noteToDelete.setId(noteId);
        noteToDelete.setImageUrl("imageUrlToDelete");

        when(noteValidator.removingNoteValidation(noteId)).thenReturn(noteToDelete);

        noteService.deleteNote(noteId);

        verify(noteValidator).removingNoteValidation(noteId);
        verify(s3client).deleteObject(bucketName, "imageUrlToDelete");
        verify(noteRepository).deleteById(noteId);
    }

    @Test
    public void testAddNoteToPlant() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", "text/plain", "some xml".getBytes());
        requestDTO.setPlantImage(file);
        when(noteMapper.toEntity(requestDTO)).thenReturn(note);
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(noteMapper.toDTO(note)).thenReturn(responseDTO);

        NoteResponseDTO result = noteService.addNoteToPlant(requestDTO);

        assertNotNull(result);
        verify(noteValidator).noteRequestDTOValidation(requestDTO);
        verify(s3client).putObject(any(PutObjectRequest.class));
    }


    @Test
    public void testFindAllNotesByPlantId() {
        Long plantId = 1L;
        when(noteRepository.findAllByPlantId(plantId)).thenReturn(Arrays.asList(note));
        when(noteMapper.toDTO(note)).thenReturn(responseDTO);

        List<NoteResponseDTO> results = noteService.findAllNotesByPlantId(plantId);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        verify(noteValidator).plantToAddNoteValidation(plantId);
        verify(noteRepository).findAllByPlantId(plantId);
    }


}
