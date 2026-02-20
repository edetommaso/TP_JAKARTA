package com.example.tpjakarta.services;

import com.example.tpjakarta.api.dto.RegisterDTO;
import com.example.tpjakarta.api.exception.BusinessException;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void register(RegisterDTO registerDTO) {
        if (registerDTO == null) {
            throw new BusinessException("Registration data is required");
        }
        
        if (userRepository.findByUsername(registerDTO.getUsername()) != null) {
            throw new BusinessException("Username already exists");
        }
        
        if (userRepository.findByEmail(registerDTO.getEmail()) != null) {
            throw new BusinessException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword()); // Plain text as per DbLoginModule
        user.setCreatedAt(Timestamp.from(Instant.now()));
        
        userRepository.create(user);
    }
}
