package com.example.tpjakarta.api.mapper;

import com.example.tpjakarta.api.dto.CategoryDTO;
import com.example.tpjakarta.beans.Category;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    
    @Mapping(target = "annonces", ignore = true)
    Category toEntity(CategoryDTO dto);
}
