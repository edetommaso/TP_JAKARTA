package com.example.tpjakarta.services;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.repositories.CategoryRepository;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnonceServiceTest {

    @Mock
    private AnnonceRepository annonceRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;

    private AnnonceService annonceService;

    @BeforeEach
    void setUp() {
        annonceService = new AnnonceService(annonceRepository, categoryRepository, userRepository);
    }

    @Test
    void create_ShouldCreateAnnonce_WhenValid() {
        // Arrange
        Long userId = 1L;
        AnnonceCreateDTO dto = AnnonceCreateDTO.builder()
                .title("Test Title")
                .description("Desc")
                .adress("Paris")
                .mail("test@test.com")
                .build();
        
        User author = new User();
        author.setId(userId);
        
        when(userRepository.findById(userId)).thenReturn(author);
        
        // Act
        AnnonceDTO result = annonceService.create(dto, userId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(annonceRepository).create(any(Annonce.class));
    }

    @Test
    void update_ShouldThrowException_WhenAnnonceIsPublished() {
        // Arrange
        Long annonceId = 1L;
        Long userId = 1L;
        
        Annonce existing = new Annonce();
        existing.setId(annonceId);
        existing.setStatus(AnnonceStatus.PUBLISHED);
        User author = new User();
        author.setId(userId);
        existing.setAuthor(author);

        when(annonceRepository.findById(annonceId)).thenReturn(existing);

        AnnonceCreateDTO dto = new AnnonceCreateDTO();

        // Act & Assert
        Exception exception = assertThrows(com.example.tpjakarta.api.exception.BusinessException.class, () -> {
            annonceService.update(annonceId, dto, userId);
        });
        
        assertEquals("Cannot modify a PUBLISHED annonce", exception.getMessage());
    }

    @Test
    void delete_ShouldThrowException_WhenNotArchived() {
        // Arrange
        Long annonceId = 1L;
        Long userId = 1L;
        
        Annonce existing = new Annonce();
        existing.setId(annonceId);
        existing.setStatus(AnnonceStatus.DRAFT); // Not ARCHIVED
        User author = new User();
        author.setId(userId);
        existing.setAuthor(author);

        when(annonceRepository.findById(annonceId)).thenReturn(existing);

        // Act & Assert
        Exception exception = assertThrows(com.example.tpjakarta.api.exception.BusinessException.class, () -> {
            annonceService.delete(annonceId, userId);
        });
        
        assertEquals("Annonce must be ARCHIVED before deletion", exception.getMessage());
    }
}
