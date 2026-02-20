package com.example.tpjakarta.api.config;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@OpenAPIDefinition(
    info = @Info(
        title = "Annonce API",
        version = "1.0",
        description = "API for managing annonces"
    ),
    servers = {
        @Server(url = "/api", description = "Default Server")
    },
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
// @ApplicationPath("/api") // Removed to avoid conflict with web.xml
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        // Register resources
        resources.add(com.example.tpjakarta.api.resource.AnnonceResource.class);
        resources.add(com.example.tpjakarta.api.resource.AuthResource.class);
        resources.add(com.example.tpjakarta.api.resource.HelloWorldResource.class);
        
        // Register providers/filters
        resources.add(com.example.tpjakarta.api.exception.GlobalExceptionMapper.class);
        resources.add(com.example.tpjakarta.api.exception.ValidationExceptionMapper.class);
        resources.add(com.example.tpjakarta.api.filters.AuthenticationFilter.class);
        resources.add(org.glassfish.jersey.jackson.JacksonFeature.class);

        // Register OpenAPI
        resources.add(com.example.tpjakarta.api.resource.ApiListingResource.class);
        
        // Register Categories
        resources.add(com.example.tpjakarta.api.resource.CategoryResource.class);
        
        return resources;
    }
}
