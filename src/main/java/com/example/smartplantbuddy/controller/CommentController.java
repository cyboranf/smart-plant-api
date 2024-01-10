package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.comment.CommentRequestDTO;
import com.example.smartplantbuddy.dto.comment.CommentResponseDTO;
import com.example.smartplantbuddy.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Adds a new comment to a specific plant.
     *
     * @param commentRequestDTO The DTO containing the comment details.
     * @return A ResponseEntity with the created comment response DTO.
     */
    @PostMapping("/add")
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO responseDTO = commentService.addCommentToPlant(commentRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all comments for a specific plant.
     *
     * @param plantId The ID of the plant for which comments are to be retrieved.
     * @return A ResponseEntity with a list of comment response DTOs.
     */
    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPlantId(@PathVariable Long plantId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPlantId(plantId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Updates an existing comment by its ID.
     *
     * @param commentId The ID of the comment to be updated.
     * @param commentRequestDTO The DTO containing the updated comment details.
     * @return A ResponseEntity with the updated comment response DTO.
     */
    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO responseDTO = commentService.updateComment(commentId, commentRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId The ID of the comment to be deleted.
     * @return A ResponseEntity indicating the result of the deletion operation.
     */
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
