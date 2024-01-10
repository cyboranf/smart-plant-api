package com.example.smartplantbuddy.dto.comment;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String timestamp;
    private Long userId; // user who made the comment
    private String username; // (login) username of user who made the comment
    private Long plantId;
    private Long noteId;
}
