package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.LoginDTO;
import com.example.tpjakarta.api.dto.TokenDTO;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.sql.Timestamp;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Gestion de l'inscription et de la connexion")
public class AuthController { // Renamed from AuthResource

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        // 1. Fetch user by username
        User user = userRepository.findByUsername(loginDTO.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // 2. Validate password with PasswordEncoder
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // 3. Determine roles (e.g., admin gets ADMIN, others get USER)
        String role = "admin".equalsIgnoreCase(user.getUsername()) ? "ROLE_ADMIN" : "ROLE_USER";

        // 4. Generate JWT token
        String token = jwtService.generateToken(user.getUsername(), user.getId(), Collections.singletonList(role));

        // 5. Return Token
        return ResponseEntity.ok(new TokenDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody com.example.tpjakarta.api.dto.RegisterDTO registerDTO) {
        if (registerDTO == null || registerDTO.getUsername() == null || registerDTO.getEmail() == null
                || registerDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username, email and password are required");
        }

        // 1. Check if username exists
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        // 2. Check if email exists
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken");
        }

        // 3. Create and Save User
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
