package com.example.tpjakarta.api.mapper;

import com.example.tpjakarta.api.dto.UserDTO;
import com.example.tpjakarta.beans.User;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "annonces", ignore = true)
    User toEntity(UserDTO dto);
}
