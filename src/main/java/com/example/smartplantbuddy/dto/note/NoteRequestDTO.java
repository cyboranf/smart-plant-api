package com.example.smartplantbuddy.dto.note;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NoteRequestDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;
    private String note;
    @Nullable
    private Long plantId;
    @Nullable
    private MultipartFile plantImage;
}
