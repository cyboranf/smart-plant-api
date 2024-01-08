package com.example.smartplantbuddy.dto.gallery;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class GalleryRequestDTO {
    private MultipartFile plantImage;
    private Long plantId;
    // url will be set as plant
}
