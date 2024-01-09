package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.exception.note.NoteNotFoundException;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.exception.plant.PlantNotFoundException;
import com.example.smartplantbuddy.mapper.NoteMapper;
import com.example.smartplantbuddy.model.Note;
import com.example.smartplantbuddy.model.Plant;
import com.example.smartplantbuddy.repository.NoteRepository;
import com.example.smartplantbuddy.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides services for managing notes associated with plants.
 * This service handles the creation, updating, and deletion of plant notes,
 * as well as uploading associated images to Amazon S3 and retrieving notes by plant ID.
 * Images are managed in the cloud, providing a centralized location for note-related media.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final PlantRepository plantRepository;
    private final AmazonS3 s3client;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper, PlantRepository plantRepository, AmazonS3 s3client) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.plantRepository = plantRepository;
        this.s3client = s3client;
    }
    /**
     * Creates a new note associated with a plant and optionally uploads an image to Amazon S3.
     *
     * @param requestDTO The DTO containing the note details and optional image.
     * @return The response DTO containing details of the created note.
     * @throws IOException if an I/O error occurs during file upload.
     */
    public NoteResponseDTO addNoteToPlant(NoteRequestDTO requestDTO) throws IOException {
        MultipartFile file = requestDTO.getPlantImage();
        Note note = noteMapper.toEntity(requestDTO);

        if (file != null && !file.isEmpty()) {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String s3Path = uploadFileToS3(file, fileName);
            note.setImageUrl(s3Path);
        }

        Note savedNote = noteRepository.save(note);
        return noteMapper.toDTO(savedNote);
    }
    /**
     * Generates a unique file name for an image to prevent collisions in the storage bucket.
     *
     * @param originalFilename The original filename of the uploaded file.
     * @return A unique file name.
     */
    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
    /**
     * Uploads a file to Amazon S3 in the bucket configured for the application.
     *
     * @param file The multipart file to be uploaded.
     * @param fileName The name of the file to be used in S3.
     * @return The URL of the uploaded file.
     * @throws IOException if an I/O error occurs during file upload.
     */
    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        String s3Path = "plants/notes/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, tempFile));

        tempFile.deleteOnExit();

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Path;
    }
    /**
     * Retrieves all notes associated with a specific plant ID.
     *
     * @param plantId The ID of the plant whose notes are to be retrieved.
     * @return A list of note response DTOs containing the details of each note.
     */
    public List<NoteResponseDTO> findAllNotesByPlantId(Long plantId) {
        List<Note> notes = noteRepository.findAllByPlantId(plantId);
        return notes.stream().map(noteMapper::toDTO).collect(Collectors.toList());
    }
    /**
     * Updates an existing note by its ID. If a new image is provided, the old one is replaced on Amazon S3.
     *
     * @param noteId The ID of the note to update.
     * @param requestDTO The DTO containing the updated note details and optional new image.
     * @return The response DTO containing details of the updated note.
     * @throws IOException if an I/O error occurs during file upload.
     */
    public NoteResponseDTO updateNote(Long noteId, NoteRequestDTO requestDTO) throws IOException {
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));

        // Check if a new image is provided
        MultipartFile file = requestDTO.getPlantImage();
        if (file != null && !file.isEmpty()) {
            // If the note already has an image, delete the old image from S3
            if (existingNote.getImageUrl() != null && !existingNote.getImageUrl().isEmpty()) {
                deleteFileFromS3(existingNote.getImageUrl());
            }
            // Upload the new image to S3 and set the new image URL
            String newFileName = generateUniqueFileName(file.getOriginalFilename());
            String newS3Path = uploadFileToS3(file, newFileName);
            existingNote.setImageUrl(newS3Path);
        }

        existingNote.setDate(requestDTO.getDate() != null ? requestDTO.getDate() : existingNote.getDate());
        existingNote.setTime(requestDTO.getTime() != null ? requestDTO.getTime() : existingNote.getTime());
        existingNote.setNote(requestDTO.getNote() != null ? requestDTO.getNote() : existingNote.getNote());


        Note updatedNote = noteRepository.save(existingNote);

        return noteMapper.toDTO(updatedNote);
    }

    /**
     * Deletes a file from Amazon S3 based on the URL of the file.
     * @param fileUrl
     */
    private void deleteFileFromS3(String fileUrl) {
        String s3Key = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        s3client.deleteObject(bucketName, s3Key);
    }

    /**
     * Deletes an existing note and its associated image from Amazon S3 by note ID.
     *
     * @param noteId The ID of the note to be deleted.
     */
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));
        if (note.getImageUrl() != null) {
            deleteFileFromS3(note.getImageUrl());
        }
        noteRepository.deleteById(noteId);
    }
}
