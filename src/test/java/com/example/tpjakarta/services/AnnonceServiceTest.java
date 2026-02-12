package com.example.tpjakarta.services;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnonceServiceTest {

    @Mock
    private AnnonceRepository annonceRepository;

    @InjectMocks
    private AnnonceService annonceService;

    @Test
    void publishAnnonce_Success_WhenUserIsAuthor() {
        // Arrange
        User author = new User();
        author.setId(1L);
        author.setUsername("author");

        Annonce annonce = new Annonce();
        annonce.setAuthor(author);
        annonce.setStatus(AnnonceStatus.DRAFT);

        // Act
        boolean result = annonceService.publishAnnonce(annonce, author);

        // Assert
        assertTrue(result, "Service should return true for successful publication");
        assertEquals(AnnonceStatus.PUBLISHED, annonce.getStatus(), "Annonce status should be set to PUBLISHED");
        verify(annonceRepository, times(1)).update(annonce); // Verify that the repository's update method was called once
    }

    @Test
    void publishAnnonce_Failure_WhenUserIsNotAuthor() {
        // Arrange
        User author = new User();
        author.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Annonce annonce = new Annonce();
        annonce.setAuthor(author);
        annonce.setStatus(AnnonceStatus.DRAFT);

        // Act
        boolean result = annonceService.publishAnnonce(annonce, anotherUser);

        // Assert
        assertFalse(result, "Service should return false when user is not the author");
        assertEquals(AnnonceStatus.DRAFT, annonce.getStatus(), "Annonce status should remain DRAFT");
        verify(annonceRepository, never()).update(annonce); // Verify that update was never called
    }

    @Test
    void publishAnnonce_Failure_WhenAnnonceIsNull() {
        // Arrange
        User user = new User();
        user.setId(1L);

        // Act
        boolean result = annonceService.publishAnnonce(null, user);

        // Assert
        assertFalse(result);
        verify(annonceRepository, never()).update(any(Annonce.class));
    }
}
