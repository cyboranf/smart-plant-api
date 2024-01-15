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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    void addCommentToPlant() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Test content");

        Comment comment = new Comment();
        comment.setContent(requestDTO.getContent());

        User user = new User();
        user.setLogin("testUser");

        when(commentMapper.toEntity(any(CommentRequestDTO.class))).thenReturn(comment);
        when(userRepository.findUserByLogin("testUser")).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(new CommentResponseDTO());

        CommentResponseDTO responseDTO = commentService.addCommentToPlant(requestDTO);

        assertNotNull(responseDTO);
        verify(commentValidator).commentRequestDTOValidation(requestDTO);
        verify(commentRepository).save(comment);
        verify(commentMapper).toDTO(comment);
    }

    @Test
    void getCommentsByPlantId() {
        Long plantId = 1L;
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        when(commentRepository.findByPlantId(plantId)).thenReturn(comments);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(new CommentResponseDTO());

        List<CommentResponseDTO> responseDTOs = commentService.getCommentsByPlantId(plantId);

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(commentValidator).getCommentsByPlantIdValidation(plantId);
        verify(commentRepository).findByPlantId(plantId);
    }

    @Test
    void updateComment() {
        Long commentId = 1L;
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Updated content");

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setContent("Original content");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(new CommentResponseDTO());

        CommentResponseDTO responseDTO = commentService.updateComment(commentId, requestDTO);    // Assert
        assertNotNull(responseDTO);
        verify(commentValidator).commentRequestDTOValidation(requestDTO);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(existingComment);
        verify(commentMapper).toDTO(existingComment);
        assertEquals("Updated content", existingComment.getContent());
    }

    @Test
    void deleteComment() {
        Long commentId = 1L;
        when(commentRepository.existsById(commentId)).thenReturn(true);

        commentService.deleteComment(commentId);

        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void deleteComment_ThrowsException() {
        Long commentId = 1L;
        when(commentRepository.existsById(commentId)).thenReturn(false);

        Exception exception = assertThrows(CommentNotFoundException.class, () -> {
            commentService.deleteComment(commentId);
        });

        String expectedMessage = "Comment not found with id: " + commentId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
