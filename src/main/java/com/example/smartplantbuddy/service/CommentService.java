package com.example.smartplantbuddy.service;

import com.example.smartplantbuddy.dto.comment.CommentRequestDTO;
import com.example.smartplantbuddy.dto.comment.CommentResponseDTO;
import com.example.smartplantbuddy.exception.comment.CommentNotFoundException;
import com.example.smartplantbuddy.mapper.CommentMapper;
import com.example.smartplantbuddy.model.Comment;
import com.example.smartplantbuddy.model.User;
import com.example.smartplantbuddy.repository.CommentRepository;
import com.example.smartplantbuddy.repository.UserRepository;
import com.example.smartplantbuddy.validation.CommentValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class CommentService {
    private final CommentValidator commentValidator;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public CommentService(CommentValidator commentValidator, CommentRepository commentRepository, CommentMapper commentMapper, UserRepository userRepository) {
        this.commentValidator = commentValidator;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    /**
     * Adds a comment to a plant.
     *
     * @param commentRequestDTO The DTO containing the comment details.
     * @return The response DTO of the created comment.
     */
    public CommentResponseDTO addCommentToPlant(CommentRequestDTO commentRequestDTO) {
        commentValidator.commentRequestDTOValidation(commentRequestDTO);
        Comment comment = commentMapper.toEntity(commentRequestDTO);
        comment.setUser(getAuthenticatedUser());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    /**
     * Retrieves all comments associated with a specific plant.
     *
     * @param plantId The ID of the plant.
     * @return A list of comment response DTOs.
     */
    public List<CommentResponseDTO> getCommentsByPlantId(Long plantId) {
        commentValidator.getCommentsByPlantIdValidation(plantId);
        List<Comment> comments = commentRepository.findByPlantId(plantId);
        return comments.stream().map(commentMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Updates an existing comment.
     *
     * @param commentId         The ID of the comment to update.
     * @param commentRequestDTO The DTO containing the updated comment details.
     * @return The response DTO of the updated comment.
     */
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        commentValidator.commentRequestDTOValidation(commentRequestDTO);
        existingComment.setContent(commentRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.toDTO(updatedComment);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId The ID of the comment to delete.
     */
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return The authenticated User entity.
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByLogin(username);
        return user;
    }
}