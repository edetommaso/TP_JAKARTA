package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.CategoryDTO;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Catégories", description = "Gestion des types d'annonces")
public class CategoryResource {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryResource(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryDTO == null || categoryDTO.getLabel() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Category category = new Category();
        category.setLabel(categoryDTO.getLabel());

        category = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryDTO(category.getId(), category.getLabel()));
    }
}
