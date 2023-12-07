package com.example.smartplantbuddy.dto.note;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NoteRequestDTO {
    private LocalDate date;
    private LocalTime time;
    private String note;
    private Long plantId;
    @Nullable
    private MultipartFile plantImage;
}
