package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnnonceRepositoryTest {

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createAndFind() {
        // Create User
        User user = new User();
        user.setUsername("testuser_repo");
        user.setEmail("test_repo@test.com");
        user.setPassword("password");
        userRepository.save(user);

        // Create Annonce
        Annonce annonce = new Annonce();
        annonce.setTitle("Test Annonce Repo");
        annonce.setDescription("Description Repo");
        annonce.setAdress("Address Repo");
        annonce.setMail("contact_repo@test.com");
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setDate(Timestamp.from(Instant.now()));
        annonce.setAuthor(user);

        annonceRepository.save(annonce);

        assertNotNull(annonce.getId());

        Optional<Annonce> found = annonceRepository.findById(annonce.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Annonce Repo", found.get().getTitle());
    }

    @Test
    void findAll_ShouldReturnList() {
        List<Annonce> list = annonceRepository.findAll();
        assertNotNull(list);
    }
}

