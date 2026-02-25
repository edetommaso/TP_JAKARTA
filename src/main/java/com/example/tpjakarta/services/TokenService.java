package com.example.tpjakarta.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {

    // Singleton instance
    private static final TokenService INSTANCE = new TokenService();

    // Map Token -> UserId
    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();
    // private final UserRepository userRepository; // Commented out as per instruction

    private TokenService() {
        // this.userRepository = new UserRepository(); // Commented out as per instruction
    }

    public static TokenService getInstance() {
        return INSTANCE;
    }

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, userId);
        return token;
    }

    public Long validateToken(String token) {
        // Removed DB lookup since we can't inject JpaRepository into normal singleton cleanly
        /*
        // This part seems to be from a different context (e.g., an authentication filter)
        // and is not directly applicable to TokenService's validateToken method as it stands.
        // Keeping the original validateToken logic for now.
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Return UserPrincipal with ID extracted from User
            return new UserPrincipal(username, user.getId());
        }
        // Fallback: just return principal with username
        return new UserPrincipal(username, -1L);
        */
        return tokenStore.get(token);
    }
    
    public void invalidateToken(String token) {
        tokenStore.remove(token);
    }
}
```
