package com.example.smartplantbuddy.dto.note;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NoteResponseDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String note;
    private Long plantId;
    private String plantImageUrl;
}
