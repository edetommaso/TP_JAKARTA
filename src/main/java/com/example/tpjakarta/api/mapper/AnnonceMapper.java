package com.example.tpjakarta.api.mapper;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.utils.AnnonceStatus;

import java.sql.Timestamp;
import java.time.Instant;

public class AnnonceMapper {

    public static AnnonceDTO toDTO(Annonce annonce) {
        if (annonce == null) {
            return null;
        }
        return AnnonceDTO.builder()
                .id(annonce.getId())
                .title(annonce.getTitle())
                .description(annonce.getDescription())
                .adress(annonce.getAdress())
                .mail(annonce.getMail())
                .date(annonce.getDate())
                .status(annonce.getStatus())
                .authorId(annonce.getAuthor() != null ? annonce.getAuthor().getId() : null)
                .authorName(annonce.getAuthor() != null ? annonce.getAuthor().getUsername() : null) // Assuming User has username
                .categoryId(annonce.getCategory() != null ? annonce.getCategory().getId() : null)
                .categoryName(annonce.getCategory() != null ? annonce.getCategory().getLabel() : null) // Assuming Category has label
                .build();
    }

    public static Annonce toEntity(AnnonceCreateDTO dto, User author, Category category) {
        if (dto == null) {
            return null;
        }
        Annonce annonce = new Annonce();
        annonce.setTitle(dto.getTitle());
        annonce.setDescription(dto.getDescription());
        annonce.setAdress(dto.getAdress());
        annonce.setMail(dto.getMail());
        annonce.setDate(Timestamp.from(Instant.now()));
        annonce.setStatus(AnnonceStatus.PUBLISHED); // Default status
        annonce.setAuthor(author);
        annonce.setCategory(category);
        return annonce;
    }
    
    public static void updateEntity(Annonce existing, AnnonceCreateDTO dto, Category category) {
        if (dto == null || existing == null) return;
        
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getAdress() != null) existing.setAdress(dto.getAdress());
        if (dto.getMail() != null) existing.setMail(dto.getMail());
        if (category != null) existing.setCategory(category);
    }
}
