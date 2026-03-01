package com.example.tpjakarta.services;

import com.example.tpjakarta.api.dto.RegisterDTO;
import com.example.tpjakarta.api.exception.BusinessException;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterDTO registerDTO) {
        if (registerDTO == null) {
            throw new BusinessException("Registration data is required");
        }
        
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new BusinessException("Username already exists");
        }
        
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new BusinessException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword()); // Plain text as per DbLoginModule
        user.setCreatedAt(Timestamp.from(Instant.now()));
        
        userRepository.save(user);
    }
}
