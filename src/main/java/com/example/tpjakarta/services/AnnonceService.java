package com.example.tpjakarta.services;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.api.mapper.AnnonceMapper;
import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.repositories.CategoryRepository;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.repositories.AnnonceSpecifications;
import com.example.tpjakarta.utils.AnnonceStatus;
import com.example.tpjakarta.utils.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AnnonceMapper annonceMapper;

    public AnnonceService(AnnonceRepository annonceRepository, 
                          CategoryRepository categoryRepository,
                          UserRepository userRepository,
                          AnnonceMapper annonceMapper) {
        this.annonceRepository = annonceRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.annonceMapper = annonceMapper;
    }

    public Page<AnnonceDTO> searchDynamic(String q, AnnonceStatus status, Long categoryId, Long authorId, Timestamp fromDate, Timestamp toDate, String sortRaw, int page, int size) {
        // Introspection Constraint: Validate sort properties
        Sort sort = Sort.unsorted();
        if (sortRaw != null && !sortRaw.isEmpty()) {
            String[] sortParams = sortRaw.split(",");
            String sortField = sortParams[0];
            String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";

            if (ReflectionUtils.isValidAnnonceSortField(sortField)) {
                sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
            } else {
                throw new com.example.tpjakarta.api.exception.BusinessException("Invalid sort field: " + sortField);
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Annonce> spec = AnnonceSpecifications.search(q, status, categoryId, authorId, fromDate, toDate);

        Page<Annonce> annonces = annonceRepository.findAll(spec, pageable);
        return annonces.map(annonceMapper::toDTO);
    }

    public AnnonceDTO findById(Long id) {
        Annonce annonce = annonceRepository.findById(id).orElse(null);
        if (annonce == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        return annonceMapper.toDTO(annonce);
    }
    
    public Annonce findEntityById(Long id) {
        Annonce annonce = annonceRepository.findById(id).orElse(null);
        if (annonce == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        return annonce;
    }

    public AnnonceDTO create(AnnonceCreateDTO dto, Long userId) {
        User author = userRepository.findById(userId).orElse(null);
        if (author == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("User not found with id " + userId);
        }
        
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + dto.getCategoryId());
            }
        }

        Annonce annonce = annonceMapper.toEntity(dto, author, category);
        annonce = annonceRepository.save(annonce);
        return annonceMapper.toDTO(annonce);
    }

    public AnnonceDTO update(Long id, AnnonceCreateDTO dto, Long userId) {
        Annonce existing = annonceRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only update your own annonces");
        }
        
        if (existing.getStatus() == AnnonceStatus.PUBLISHED) {
            throw new com.example.tpjakarta.api.exception.BusinessException("Cannot modify a PUBLISHED annonce");
        }

        Category category = existing.getCategory();
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + dto.getCategoryId());
            }
        }

        annonceMapper.updateEntity(existing, dto, category);
        Annonce updated = annonceRepository.save(existing);
        return annonceMapper.toDTO(updated);
    }
    
    public void delete(Long id, Long userId) {
        Annonce existing = annonceRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only delete your own annonces");
        }
        
        if (existing.getStatus() != AnnonceStatus.ARCHIVED) {
            throw new com.example.tpjakarta.api.exception.BusinessException("Annonce must be ARCHIVED before deletion");
        }
        
        annonceRepository.deleteById(id);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public void archive(Long id, Long userId) {
        Annonce existing = annonceRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        // Removed author check because Business Rule states: "seul un ADMIN peut archiver"
        
        if (existing.getStatus() == AnnonceStatus.ARCHIVED) {
            return;
        }
        
        existing.setStatus(AnnonceStatus.ARCHIVED);
        annonceRepository.save(existing);
    }

    public void publishAnnonce(Annonce annonce, User currentUser) {
        if (annonce == null) {
             throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce is null");
        }
        if (currentUser == null) {
             throw new SecurityException("User must be logged in");
        }
        if (!annonce.getAuthor().getId().equals(currentUser.getId())) {
             throw new SecurityException("You can only publish your own annonces");
        }
        
        annonce.setStatus(AnnonceStatus.PUBLISHED);
        annonceRepository.save(annonce);
    }
    
    public AnnonceDTO patch(Long id, AnnonceCreateDTO updates, Long userId) {
        Annonce existing = annonceRepository.findById(id).orElse(null);
        if (existing == null) {
             throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only patch your own annonces");
        }
        
        if (existing.getStatus() == AnnonceStatus.PUBLISHED) {
            throw new com.example.tpjakarta.api.exception.BusinessException("Cannot modify a PUBLISHED annonce");
        }

        Category category = existing.getCategory();
        if (updates.getCategoryId() != null) {
            category = categoryRepository.findById(updates.getCategoryId()).orElse(null);
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + updates.getCategoryId());
            }
        }

        annonceMapper.updateEntity(existing, updates, category);
        
        Annonce updated = annonceRepository.save(existing);
        return annonceMapper.toDTO(updated);
    }
}
