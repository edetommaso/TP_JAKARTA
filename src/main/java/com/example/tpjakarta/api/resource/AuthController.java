package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.LoginDTO;
import com.example.tpjakarta.api.dto.TokenDTO;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController { // Renamed from AuthResource

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
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

        // 2. Validate password (in a real app, use PasswordEncoder.matches())
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // 3. Determine roles (e.g., admin gets ADMIN, others get USER)
        String role = "admin".equalsIgnoreCase(user.getUsername()) ? "ROLE_ADMIN" : "ROLE_USER";

        // 4. Generate JWT token
        String token = jwtService.generateToken(user.getUsername(), user.getId(), Collections.singletonList(role));

        // 5. Return Token
        return ResponseEntity.ok(new TokenDTO(token));
    }

    // Keeping register just to match previous layout
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody com.example.tpjakarta.api.dto.RegisterDTO registerDTO) {
        // Normally this goes to a UserService, this dummy implementation skips for brevity on Exercise 4
        // which focuses strictly on /api/auth/login and JWT generation.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
