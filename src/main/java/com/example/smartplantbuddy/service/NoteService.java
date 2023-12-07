package com.example.smartplantbuddy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.smartplantbuddy.dto.note.NoteRequestDTO;
import com.example.smartplantbuddy.dto.note.NoteResponseDTO;
import com.example.smartplantbuddy.exception.plant.ImageEmptyException;
import com.example.smartplantbuddy.mapper.NoteMapper;
import com.example.smartplantbuddy.model.Note;
import com.example.smartplantbuddy.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final AmazonS3 s3client;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper, AmazonS3 s3client) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.s3client = s3client;
    }

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

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        String s3Path = "plants/notes/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, tempFile));

        tempFile.deleteOnExit();

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Path;
    }
}
