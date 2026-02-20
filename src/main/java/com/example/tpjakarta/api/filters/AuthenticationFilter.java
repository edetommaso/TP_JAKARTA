package com.example.tpjakarta.api.filters;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.services.TokenService;
import com.example.tpjakarta.api.security.UserPrincipal;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    private final TokenService tokenService = TokenService.getInstance();
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Public paths
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();
        
        // Exercise 6: "Refus accès non authentifié"
        // Allow login/register and OpenAPI
        if (path.contains("auth/login") || path.contains("auth/register") || path.contains("openapi")) {
            return;
        }
        
        // Public resources (HelloWorldResource)
        if (path.contains("helloWorld") || path.contains("params")) {
            return;
        }
        
        // Allow GET on annonces (Exercice 2: "GET /api/annonces")
        if (path.startsWith("annonces") && "GET".equalsIgnoreCase(method)) {
             return;
        }
        
        // Allow GET on categories (for dropdowns etc)
        if (path.startsWith("categories") && "GET".equalsIgnoreCase(method)) {
             return;
        }

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
             abortWithUnauthorized(requestContext);
             return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            // JAAS Token Validation
            LoginContext loginContext = new LoginContext("MasterAnnonceToken", new CallbackHandler() {
                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback callback : callbacks) {
                        if (callback instanceof NameCallback) {
                             // Pass token as "Name"
                             ((NameCallback) callback).setName(token);
                        }
                    }
                }
            });
            
            loginContext.login();
            
            Subject subject = loginContext.getSubject();
            UserPrincipal userPrincipal = subject.getPrincipals(UserPrincipal.class).stream().findFirst().orElse(null);
            
            if (userPrincipal == null) {
                 abortWithUnauthorized(requestContext);
                 return;
            }
            
            final String userIdStr = String.valueOf(userPrincipal.getUserId());

            // Override the SecurityContext
            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {
                    return () -> userIdStr; // Name = UserId
                }

                @Override
                public boolean isUserInRole(String role) {
                    // Check principals for RolePrincipal
                    if (subject == null) return false;
                    
                    return subject.getPrincipals(com.example.tpjakarta.api.security.RolePrincipal.class).stream()
                            .anyMatch(rolePrincipal -> rolePrincipal.getName().equals(role));
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return AUTHENTICATION_SCHEME;
                }
            });

        } catch (Exception e) {
            e.printStackTrace(); // Log error
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                        .build());
    }
}
