package com.example.tpjakarta.api.dto;

import com.example.tpjakarta.utils.AnnonceStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AnnonceDTO {
    private Long id;
    private String title;
    private String description;
    private String adress;
    private String mail;
    private Timestamp date;
    private AnnonceStatus status;
    private Long authorId;
    private String authorName; // Bonus: simpler for frontend
    private Long categoryId;
    private String categoryName; // Bonus
}
