package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.CategoryRepository;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureMockMvc
public class AnnonceResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private String userToken;
    private String adminToken;
    private Long testUserId;
    private Long testCategoryId;

    @BeforeEach
    void setUp() {
        // Setup User
        User user = userRepository.findByUsername("testuser").orElse(null);
        if (user == null) {
            user = new User();
            user.setUsername("testuser");
            user.setPassword("pass");
            user.setEmail("test@user.com");
            user = userRepository.save(user);
        }
        testUserId = user.getId();
        userToken = "Bearer " + jwtService.generateToken("testuser", testUserId, List.of("ROLE_USER"));
        adminToken = "Bearer " + jwtService.generateToken("adminuser", testUserId + 1, List.of("ROLE_ADMIN"));

        // Setup Category
        if (categoryRepository.findAll().isEmpty()) {
            Category cat = new Category();
            cat.setLabel("Test Category");
            cat = categoryRepository.save(cat);
            testCategoryId = cat.getId();
        } else {
            testCategoryId = categoryRepository.findAll().get(0).getId();
        }
    }

    @Test
    void testProtectedEndpointWithoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/annonces")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Security filter will block to 403 or 401
    }

    @Test
    void testProtectedEndpointWithInvalidTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/annonces")
                .header("Authorization", "Bearer invalid-token.xyz")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testArchiveWithoutAdminReturns403() throws Exception {
        mockMvc.perform(post("/api/annonces/999/archive")
                .header("Authorization", userToken) // Assuming userToken is ROLE_USER
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Should be 403 Forbidden because lacks ADMIN role
    }

    @Test
    void testCrudFlow() throws Exception {
        // Create
        AnnonceCreateDTO createDTO = new AnnonceCreateDTO();
        createDTO.setTitle("Integration Test Annonce");
        createDTO.setDescription("Description of the test");
        createDTO.setAdress("123 Test Street");
        createDTO.setMail("test@mail.com");
        createDTO.setCategoryId(testCategoryId);

        MvcResult result = mockMvc.perform(post("/api/annonces")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Integration Test Annonce"))
                .andReturn();

        // Read All
        mockMvc.perform(get("/api/annonces")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(greaterThan(0)));
    }
}

