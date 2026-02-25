package com.example.tpjakarta.services;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.mapper.AnnonceMapper;
import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.repositories.CategoryRepository;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnnonceServiceTest {

    @Mock
    private AnnonceRepository annonceRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AnnonceMapper annonceMapper;

    @InjectMocks
    private AnnonceService annonceService;

    private User author;
    private Annonce annonce;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);

        annonce = new Annonce();
        annonce.setId(10L);
        annonce.setAuthor(author);
        annonce.setStatus(AnnonceStatus.DRAFT);
    }

    @Test
    void testModifyAnnonce_Success_WhenUserIsAuthor() {
        AnnonceCreateDTO dto = new AnnonceCreateDTO();
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));
        when(annonceRepository.save(any(Annonce.class))).thenReturn(annonce);
        
        annonceService.update(10L, dto, 1L); // Same ID as author
        verify(annonceRepository, times(1)).save(annonce);
    }

    @Test
    void testModifyAnnonce_ThrowsException_WhenUserIsNotAuthor() {
        AnnonceCreateDTO dto = new AnnonceCreateDTO();
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));

        assertThrows(SecurityException.class, () -> {
            annonceService.update(10L, dto, 99L); // Different ID
        });
        verify(annonceRepository, never()).save(any(Annonce.class));
    }

    @Test
    void testModifyAnnonce_ThrowsException_WhenAnnonceIsPublished() {
        annonce.setStatus(AnnonceStatus.PUBLISHED);
        AnnonceCreateDTO dto = new AnnonceCreateDTO();
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));

        assertThrows(com.example.tpjakarta.api.exception.BusinessException.class, () -> {
            annonceService.update(10L, dto, 1L);
        });
        verify(annonceRepository, never()).save(any(Annonce.class));
    }

    @Test
    void testArchive_Success_WhenUserIsAdmin() {
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));
        // Security logic is handled by @PreAuthorize on the endpoint/service,
        // so calling this method directly in Unit test assumes we somehow passed through security proxy.
        annonceService.archive(10L, 99L);
        
        verify(annonceRepository, times(1)).save(argThat(a -> a.getStatus() == AnnonceStatus.ARCHIVED));
    }
}
