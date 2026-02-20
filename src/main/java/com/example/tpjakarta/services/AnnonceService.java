package com.example.tpjakarta.services;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.utils.AnnonceStatus;

public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final com.example.tpjakarta.repositories.CategoryRepository categoryRepository;
    private final com.example.tpjakarta.repositories.UserRepository userRepository;

    public AnnonceService() {
        this.annonceRepository = new AnnonceRepository();
        this.categoryRepository = new com.example.tpjakarta.repositories.CategoryRepository();
        this.userRepository = new com.example.tpjakarta.repositories.UserRepository();
    }
    
    // Constructor for testing/injection if needed
    public AnnonceService(AnnonceRepository annonceRepository, 
                          com.example.tpjakarta.repositories.CategoryRepository categoryRepository,
                          com.example.tpjakarta.repositories.UserRepository userRepository) {
        this.annonceRepository = annonceRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public java.util.List<com.example.tpjakarta.api.dto.AnnonceDTO> findAll(int page, int size) {
        // Return only PUBLISHED annonces for public list
        java.util.List<Annonce> annonces = annonceRepository.search(null, null, AnnonceStatus.PUBLISHED, null, page, size);
        return annonces.stream()
                .map(com.example.tpjakarta.api.mapper.AnnonceMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public com.example.tpjakarta.api.dto.AnnonceDTO findById(Long id) {
        Annonce annonce = annonceRepository.findById(id);
        if (annonce == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        return com.example.tpjakarta.api.mapper.AnnonceMapper.toDTO(annonce);
    }
    
    public Annonce findEntityById(Long id) {
        Annonce annonce = annonceRepository.findById(id);
        if (annonce == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        return annonce;
    }

    public com.example.tpjakarta.api.dto.AnnonceDTO create(com.example.tpjakarta.api.dto.AnnonceCreateDTO dto, Long userId) {
        User author = userRepository.findById(userId);
        if (author == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("User not found with id " + userId);
        }
        
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId());
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + dto.getCategoryId());
            }
        }

        Annonce annonce = com.example.tpjakarta.api.mapper.AnnonceMapper.toEntity(dto, author, category);
        annonceRepository.create(annonce);
        return com.example.tpjakarta.api.mapper.AnnonceMapper.toDTO(annonce);
    }

    public com.example.tpjakarta.api.dto.AnnonceDTO update(Long id, com.example.tpjakarta.api.dto.AnnonceCreateDTO dto, Long userId) {
        Annonce existing = annonceRepository.findById(id);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        // Check ownership
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only update your own annonces");
        }
        
        // Check status - Exercise 7 rule: PUBLISHED cannot be modified
        if (existing.getStatus() == AnnonceStatus.PUBLISHED) {
            throw new com.example.tpjakarta.api.exception.BusinessException("Cannot modify a PUBLISHED annonce");
        }

        Category category = existing.getCategory();
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId());
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + dto.getCategoryId());
            }
        }

        com.example.tpjakarta.api.mapper.AnnonceMapper.updateEntity(existing, dto, category);
        Annonce updated = annonceRepository.update(existing);
        return com.example.tpjakarta.api.mapper.AnnonceMapper.toDTO(updated);
    }
    
    public void delete(Long id, Long userId) {
        Annonce existing = annonceRepository.findById(id);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        // Check ownership
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only delete your own annonces");
        }
        
        // Exercise 7 rule: Must be ARCHIVED before deletion
        if (existing.getStatus() != AnnonceStatus.ARCHIVED) {
            throw new com.example.tpjakarta.api.exception.BusinessException("Annonce must be ARCHIVED before deletion");
        }
        
        annonceRepository.delete(id);
    }
    
    public void archive(Long id, Long userId) {
        Annonce existing = annonceRepository.findById(id);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only archive your own annonces");
        }
        
        if (existing.getStatus() == AnnonceStatus.ARCHIVED) {
            return; // Already archived
        }
        
        existing.setStatus(AnnonceStatus.ARCHIVED);
        annonceRepository.update(existing);
    }

    /**
     * Publishes an announcement.
     * A user can only publish their own announcement.
     * @param annonce The announcement to publish.
     * @param currentUser The user attempting to publish the announcement.
     */
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
        
        // Cannot publish if archived? usually yes, but let's allow re-publish or separate logic.
        // For now standard publish.
        annonce.setStatus(AnnonceStatus.PUBLISHED);
        annonceRepository.update(annonce);
    }
    
    // Bonus: PATCH
    public com.example.tpjakarta.api.dto.AnnonceDTO patch(Long id, java.util.Map<String, Object> updates, Long userId) {
        Annonce existing = annonceRepository.findById(id);
        if (existing == null) {
             throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only patch your own annonces");
        }
        
        if (existing.getStatus() == AnnonceStatus.PUBLISHED) {
            throw new com.example.tpjakarta.api.exception.BusinessException("Cannot modify a PUBLISHED annonce");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "title": existing.setTitle((String) value); break;
                case "description": existing.setDescription((String) value); break;
                case "adress": existing.setAdress((String) value); break;
                case "mail": existing.setMail((String) value); break;
                case "categoryId": 
                    // Handle category update if needed
                    break;
            }
        });
        
        Annonce updated = annonceRepository.update(existing);
        return com.example.tpjakarta.api.mapper.AnnonceMapper.toDTO(updated);
    }
}
