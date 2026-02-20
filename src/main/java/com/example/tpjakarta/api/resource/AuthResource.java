package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.LoginDTO;
import com.example.tpjakarta.api.dto.TokenDTO;
import com.example.tpjakarta.api.security.UserPrincipal;
import com.example.tpjakarta.services.TokenService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final TokenService tokenService;
    private final com.example.tpjakarta.services.UserService userService;

    public AuthResource() {
        this.tokenService = TokenService.getInstance();
        this.userService = new com.example.tpjakarta.services.UserService();
    }

    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            // JAAS Login
            LoginContext loginContext = new LoginContext("MasterAnnonceLogin", new CallbackHandler() {
                @Override
                public void handle(Callback[] callbacks) throws java.io.IOException, UnsupportedCallbackException {
                    for (Callback callback : callbacks) {
                        if (callback instanceof NameCallback) {
                            ((NameCallback) callback).setName(loginDTO.getUsername());
                        } else if (callback instanceof PasswordCallback) {
                            ((PasswordCallback) callback).setPassword(loginDTO.getPassword().toCharArray());
                        }
                    }
                }
            });

            loginContext.login();
            
            // Authentication successful, generate token
            Subject subject = loginContext.getSubject();
            
            // Extract UserPrincipal to get userId
            UserPrincipal userPrincipal = subject.getPrincipals(UserPrincipal.class).stream().findFirst().orElse(null);
            
            if (userPrincipal != null) {
                String token = tokenService.generateToken(userPrincipal.getUserId());
                return Response.ok(new TokenDTO(token)).build();
            } else {
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("UserPrincipal not found").build();
            }

        } catch (LoginException e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(com.example.tpjakarta.api.dto.RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Response.status(Response.Status.CREATED).build();
    }
}
