package com.example.tpjakarta.api.mapper;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AnnonceMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.username", target = "authorName")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.label", target = "categoryName")
    AnnonceDTO toDTO(Annonce annonce);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "status", expression = "java(com.example.tpjakarta.utils.AnnonceStatus.PUBLISHED)")
    @Mapping(target = "date", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
    @Mapping(target = "title", source = "dto.title") // explicit when multi-source
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "adress", source = "dto.adress")
    @Mapping(target = "mail", source = "dto.mail")
    Annonce toEntity(AnnonceCreateDTO dto, User author, Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "adress", source = "dto.adress")
    @Mapping(target = "mail", source = "dto.mail")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Annonce existing, AnnonceCreateDTO dto, Category category);
}
