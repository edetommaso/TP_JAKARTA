package com.example.tpjakarta.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {

    // Singleton instance
    private static final TokenService INSTANCE = new TokenService();

    // Map Token -> UserId
    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    private TokenService() {}

    public static TokenService getInstance() {
        return INSTANCE;
    }

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, userId);
        return token;
    }

    public Long validateToken(String token) {
        return tokenStore.get(token);
    }
    
    public void invalidateToken(String token) {
        tokenStore.remove(token);
    }
}
