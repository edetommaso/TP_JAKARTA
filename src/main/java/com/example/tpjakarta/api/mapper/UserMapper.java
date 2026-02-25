package com.example.tpjakarta.api.mapper;

import com.example.tpjakarta.api.dto.UserDTO;
import com.example.tpjakarta.beans.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
}
