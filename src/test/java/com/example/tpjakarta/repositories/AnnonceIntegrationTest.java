package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.services.AnnonceService;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

public class AnnonceIntegrationTest {

    private AnnonceRepository annonceRepository;
    private UserRepository userRepository;
    private AnnonceService annonceService;
    private User testUser;

    @BeforeEach
    void setUp() {
        annonceRepository = new AnnonceRepository();
        userRepository = new UserRepository();
        annonceService = new AnnonceService(annonceRepository);

        testUser = new User();
        testUser.setUsername("integration_user" + System.nanoTime()); // Make username unique per test
        testUser.setEmail("integration" + System.nanoTime() + "@test.com");
        testUser.setPassword("password");
        userRepository.create(testUser);
    }
    
    @AfterEach
    void tearDown() {
        if (testUser != null && testUser.getId() != null) {
            // Clean up the user to avoid polluting the db for other test classes
            // Not strictly necessary with create-drop but good practice
            userRepository.delete(testUser.getId());
        }
    }

    @Test
    void testFullFlow_CreatePublishFind() {
        // 1. Create a new Annonce (as a DRAFT)
        Annonce annonce = new Annonce();
        annonce.setTitle("Integration Test Annonce");
        annonce.setDescription("A test from start to finish.");
        annonce.setAuthor(testUser);
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setAdress("addr");
        annonce.setMail("mail");
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonceRepository.create(annonce);

        assertNotNull(annonce.getId());
        assertEquals(AnnonceStatus.DRAFT, annonceRepository.findById(annonce.getId()).getStatus());

        // 2. Publish the Annonce using the service
        annonceService.publishAnnonce(annonce, testUser);

        // 3. Find the Annonce and verify its status is PUBLISHED
        Annonce publishedAnnonce = annonceRepository.findById(annonce.getId());
        assertNotNull(publishedAnnonce);
        assertEquals(AnnonceStatus.PUBLISHED, publishedAnnonce.getStatus());
    }

    @Test
    void testLazyInitializationException() {
        // 1. Create a few announcements for the user
        Annonce annonce1 = new Annonce();
        annonce1.setTitle("Annonce 1");
        annonce1.setAuthor(testUser);
        annonce1.setAdress("a"); annonce1.setMail("a"); annonce1.setDescription("a");
        annonceRepository.create(annonce1);

        Annonce annonce2 = new Annonce();
        annonce2.setTitle("Annonce 2");
        annonce2.setAuthor(testUser);
        annonce2.setAdress("b"); annonce2.setMail("b"); annonce2.setDescription("b");
        annonceRepository.create(annonce2);

        // 2. Fetch the user in a transaction, which then closes.
        User userFromDb = userRepository.findById(testUser.getId());
        assertNotNull(userFromDb);

        // 3. Now, outside of the transaction, try to access the lazy collection.
        // This should throw a LazyInitializationException.
        assertThrows(LazyInitializationException.class, () -> {
            int size = userFromDb.getAnnonces().size();
        });
    }
}

