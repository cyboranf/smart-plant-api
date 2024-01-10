package com.example.smartplantbuddy.dto.comment;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CommentRequestDTO {
    private String content;
    private Long plantId;
    @Nullable
    private Long noteId;
    // time should be set in mapper and user should be set in service class
}
