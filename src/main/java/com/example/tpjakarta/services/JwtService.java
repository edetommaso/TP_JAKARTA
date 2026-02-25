package com.example.tpjakarta.services;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    // Ideally, this key should be externalized in application.yml
    // For the TP, generating a secure key on startup is sufficient.
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    // 24 hours in milliseconds
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    /**
     * Generates a signed JWT token containing userId and roles.
     * @param username The subject of the token
     * @param userId The ID of the user
     * @param roles List of roles like ["USER", "ADMIN"]
     * @return the encoded JWT string
     */
    public String generateToken(String username, Long userId, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }
}
