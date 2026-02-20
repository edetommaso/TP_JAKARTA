package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.utils.AnnonceStatus;
import com.example.tpjakarta.utils.JPAUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnonceRepositoryTest {

    private AnnonceRepository annonceRepository;
    private UserRepository userRepository;

    @BeforeAll
    static void initConfig() {
        // Use H2 Test DB
        JPAUtils.setPersistenceUnitName("pu-test");
    }

    @BeforeEach
    void setUp() {
        annonceRepository = new AnnonceRepository();
        userRepository = new UserRepository();
    }
    
    @AfterEach
    void tearDown() {
        // Cleanup if needed
    }

    @Test
    void createAndFind() {
        // Create User
        User user = new User();
        user.setUsername("testuser_repo");
        user.setEmail("test_repo@test.com");
        user.setPassword("password");
        userRepository.create(user);
        
        // Create Annonce
        Annonce annonce = new Annonce();
        annonce.setTitle("Test Annonce Repo");
        annonce.setDescription("Description Repo");
        annonce.setAdress("Address Repo");
        annonce.setMail("contact_repo@test.com");
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setDate(Timestamp.from(Instant.now()));
        annonce.setAuthor(user);
        
        annonceRepository.create(annonce);
        
        assertNotNull(annonce.getId());
        
        Annonce found = annonceRepository.findById(annonce.getId());
        assertNotNull(found);
        assertEquals("Test Annonce Repo", found.getTitle());
    }
    
    @Test
    void findAll_ShouldReturnList() {
         List<Annonce> list = annonceRepository.findAll();
         assertNotNull(list);
    }
}
