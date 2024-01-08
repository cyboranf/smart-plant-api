package com.example.smartplantbuddy.dto.gallery;

import lombok.Data;

@Data
public class GalleryResponseDTO {
    private Long id;
    private String imageUrl;
    private Long plantId;
}
