package com.example.tpjakarta.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnonceCreateDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;
    
    @NotBlank(message = "Description is mandatory")
    private String description;
    
    @NotBlank(message = "Address is mandatory")
    private String adress;
    
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String mail;
    
    // categoryId can be null if optional, but prompt implied mandatory in some contexts. 
    // I'll leave it optional for flexibility unless constraints say otherwise.
    private Long categoryId;
}
