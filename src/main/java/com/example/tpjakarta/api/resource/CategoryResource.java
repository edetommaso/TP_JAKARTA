package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.CategoryDTO;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.repositories.CategoryRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryRepository categoryRepository;

    public CategoryResource() {
        this.categoryRepository = new CategoryRepository();
    }

    @GET
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getLabel()))
                .collect(Collectors.toList());
    }

    @POST
    // @RolesAllowed("USER") // Uncomment if we want to restrict creation
    public Response createCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null || categoryDTO.getLabel() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        Category category = new Category();
        category.setLabel(categoryDTO.getLabel());
        
        categoryRepository.create(category);
        
        return Response.status(Response.Status.CREATED).entity(new CategoryDTO(category.getId(), category.getLabel())).build();
    }
}
