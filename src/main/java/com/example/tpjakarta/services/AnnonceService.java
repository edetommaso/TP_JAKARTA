package com.example.tpjakarta.services;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.api.mapper.AnnonceMapper;
import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final com.example.tpjakarta.repositories.CategoryRepository categoryRepository;
    private final com.example.tpjakarta.repositories.UserRepository userRepository;
    private final AnnonceMapper annonceMapper;

    public AnnonceService(AnnonceMapper annonceMapper) {
        this.annonceRepository = new AnnonceRepository();
        this.categoryRepository = new com.example.tpjakarta.repositories.CategoryRepository();
        this.userRepository = new com.example.tpjakarta.repositories.UserRepository();
        this.annonceMapper = annonceMapper;
    }

    public AnnonceService(AnnonceRepository annonceRepository, 
                          com.example.tpjakarta.repositories.CategoryRepository categoryRepository,
                          com.example.tpjakarta.repositories.UserRepository userRepository,
                          AnnonceMapper annonceMapper) {
        this.annonceRepository = annonceRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.annonceMapper = annonceMapper;
    }

    public List<AnnonceDTO> findAll(int page, int size) {
        List<Annonce> annonces = annonceRepository.search(null, null, AnnonceStatus.PUBLISHED, null, page, size);
        return annonces.stream()
                .map(annonceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AnnonceDTO findById(Long id) {
        Annonce annonce = annonceRepository.findById(id);
        if (annonce == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        return annonceMapper.toDTO(annonce);
    }
    
    public Annonce findEntityById(Long id) {
        Annonce annonce = annonceRepository.findById(id);
        if (annonce == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        return annonce;
    }

    public AnnonceDTO create(AnnonceCreateDTO dto, Long userId) {
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

        Annonce annonce = annonceMapper.toEntity(dto, author, category);
        annonceRepository.create(annonce);
        return annonceMapper.toDTO(annonce);
    }

    public AnnonceDTO update(Long id, AnnonceCreateDTO dto, Long userId) {
        Annonce existing = annonceRepository.findById(id);
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
            category = categoryRepository.findById(dto.getCategoryId());
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + dto.getCategoryId());
            }
        }

        annonceMapper.updateEntity(existing, dto, category);
        Annonce updated = annonceRepository.update(existing);
        return annonceMapper.toDTO(updated);
    }
    
    public void delete(Long id, Long userId) {
        Annonce existing = annonceRepository.findById(id);
        if (existing == null) {
            throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Annonce not found with id " + id);
        }
        
        if (!existing.getAuthor().getId().equals(userId)) {
             throw new SecurityException("You can only delete your own annonces");
        }
        
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
            return;
        }
        
        existing.setStatus(AnnonceStatus.ARCHIVED);
        annonceRepository.update(existing);
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
        annonceRepository.update(annonce);
    }
    
    public AnnonceDTO patch(Long id, AnnonceCreateDTO updates, Long userId) {
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

        Category category = existing.getCategory();
        if (updates.getCategoryId() != null) {
            category = categoryRepository.findById(updates.getCategoryId());
            if (category == null) {
                 throw new com.example.tpjakarta.api.exception.ResourceNotFoundException("Category not found with id " + updates.getCategoryId());
            }
        }

        annonceMapper.updateEntity(existing, updates, category);
        
        Annonce updated = annonceRepository.update(existing);
        return annonceMapper.toDTO(updated);
    }
}
