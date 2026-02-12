package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnonceRepositoryTest {

    private AnnonceRepository annonceRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        annonceRepository = new AnnonceRepository();
        userRepository = new UserRepository();
        categoryRepository = new CategoryRepository();

        // Create and persist a test user and category
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.create(testUser);

        testCategory = new Category();
        testCategory.setLabel("Test Category");
        categoryRepository.create(testCategory);
    }

    @AfterEach
    void tearDown() {
        // The create-drop strategy handles cleanup, but we can be explicit if needed
        // For example, clean up entities that are not cleaned up by cascade
        // In this case, user and category should be cleaned up if we delete them.
        // As we want to test annonce repository, we might not want to delete user and category each time.
    }

    @Test
    void createAndFindById() {
        Annonce annonce = new Annonce();
        annonce.setTitle("Test Annonce");
        annonce.setDescription("This is a test description.");
        annonce.setAdress("123 Test Street");
        annonce.setMail("annonce@example.com");
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setAuthor(testUser);
        annonce.setCategory(testCategory);

        annonceRepository.create(annonce);

        assertNotNull(annonce.getId(), "Annonce ID should not be null after creation");

        Annonce foundAnnonce = annonceRepository.findById(annonce.getId());
        assertNotNull(foundAnnonce);
        assertEquals("Test Annonce", foundAnnonce.getTitle());
        assertEquals(testUser.getId(), foundAnnonce.getAuthor().getId());
    }

    @Test
    void update() {
        Annonce annonce = new Annonce();
        annonce.setTitle("Original Title");
        annonce.setDescription("Original description.");
        annonce.setAuthor(testUser);
        annonce.setCategory(testCategory);
        annonce.setAdress("addr");
        annonce.setMail("mail");
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonceRepository.create(annonce);

        Annonce foundAnnonce = annonceRepository.findById(annonce.getId());
        foundAnnonce.setTitle("Updated Title");
        annonceRepository.update(foundAnnonce);

        Annonce updatedAnnonce = annonceRepository.findById(annonce.getId());
        assertEquals("Updated Title", updatedAnnonce.getTitle());
    }

    @Test
    void delete() {
        Annonce annonce = new Annonce();
        annonce.setTitle("To Be Deleted");
        annonce.setDescription("This will be deleted.");
        annonce.setAuthor(testUser);
        annonce.setCategory(testCategory);
        annonce.setAdress("addr");
        annonce.setMail("mail");
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonceRepository.create(annonce);

        Long annonceId = annonce.getId();
        assertNotNull(annonceRepository.findById(annonceId));

        annonceRepository.delete(annonceId);
        assertNull(annonceRepository.findById(annonceId));
    }

    @Test
    void search() {
        Annonce annonce1 = new Annonce();
        annonce1.setTitle("First Searchable Annonce");
        annonce1.setDescription("Unique keyword one");
        annonce1.setAuthor(testUser);
        annonce1.setCategory(testCategory);
        annonce1.setAdress("addr");
        annonce1.setMail("mail");
        annonce1.setDate(new Timestamp(System.currentTimeMillis()));
        annonce1.setStatus(AnnonceStatus.PUBLISHED);
        annonceRepository.create(annonce1);

        Annonce annonce2 = new Annonce();
        annonce2.setTitle("Second Annonce");
        annonce2.setDescription("Unique keyword two");
        annonce2.setAuthor(testUser);
        annonce2.setCategory(testCategory);
        annonce2.setAdress("addr");
        annonce2.setMail("mail");
        annonce2.setDate(new Timestamp(System.currentTimeMillis()));
        annonce2.setStatus(AnnonceStatus.DRAFT);
        annonceRepository.create(annonce2);

        // Search by keyword
        List<Annonce> results = annonceRepository.search("keyword one", null, null, testUser, 1, 10);
        assertEquals(1, results.size());
        assertEquals("First Searchable Annonce", results.get(0).getTitle());

        // Search by status
        List<Annonce> resultsByStatus = annonceRepository.search(null, null, AnnonceStatus.DRAFT, testUser, 1, 10);
        assertEquals(1, resultsByStatus.size());
        assertEquals("Second Annonce", resultsByStatus.get(0).getTitle());

        // Search by category
        List<Annonce> resultsByCategory = annonceRepository.search(null, testCategory, null, testUser, 1, 10);
        assertEquals(2, resultsByCategory.size());
    }
}
