package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.config.RestApplication;
import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.api.dto.LoginDTO;
import com.example.tpjakarta.api.dto.TokenDTO;
import com.example.tpjakarta.api.exception.GlobalExceptionMapper;
import com.example.tpjakarta.api.filters.AuthenticationFilter;
import com.example.tpjakarta.api.resource.AnnonceResource;
import com.example.tpjakarta.api.resource.AuthResource;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.utils.JPAUtils;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnonceResourceIT extends JerseyTest {

    @BeforeAll
    public static void setupDB() {
        JPAUtils.setPersistenceUnitName("pu-test");
        
        // Seed a user for testing
        UserRepository repo = new UserRepository();
        User user = repo.findByUsername("it_user");
        if (user == null) {
            user = new User();
            user.setUsername("it_user");
            user.setPassword("password");
            user.setEmail("it@test.com");
            repo.create(user);
        }
    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(
                AnnonceResource.class, 
                AuthResource.class, 
                AuthenticationFilter.class, 
                GlobalExceptionMapper.class
        );
    }

    @Test
    public void testCreateAnnonceFlow() {
        // 1. Login
        LoginDTO loginDTO = new LoginDTO("it_user", "password");
        Response loginResponse = target("/login").request().post(Entity.json(loginDTO));
        
        assertEquals(200, loginResponse.getStatus());
        TokenDTO tokenDTO = loginResponse.readEntity(TokenDTO.class);
        assertNotNull(tokenDTO.getToken());
        String token = tokenDTO.getToken();

        // 2. Create Annonce (with Token)
        AnnonceCreateDTO createDTO = AnnonceCreateDTO.builder()
                .title("IT Annonce")
                .description("IT Description")
                .adress("IT Address")
                .mail("it@test.com")
                .build();

        Response createResponse = target("/annonces")
                .request()
                .header("Authorization", "Bearer " + token)
                .post(Entity.json(createDTO));

        assertEquals(201, createResponse.getStatus());
        AnnonceDTO created = createResponse.readEntity(AnnonceDTO.class);
        assertNotNull(created.getId());
        assertEquals("IT Annonce", created.getTitle());
    }
    
    @Test
    public void testGetAnnonces_Public() {
        Response response = target("/annonces").request().get();
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testCreateAnnonce_Unauthorized() {
        AnnonceCreateDTO createDTO = AnnonceCreateDTO.builder()
                .title("Unauthorized Annonce")
                .description("Desc")
                .adress("Addr")
                .mail("mail@test.com")
                .build();

        Response response = target("/annonces")
                .request()
                // No Authorization Header
                .post(Entity.json(createDTO));

        assertEquals(401, response.getStatus());
    }
}
