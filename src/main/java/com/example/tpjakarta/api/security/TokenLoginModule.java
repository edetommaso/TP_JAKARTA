package com.example.tpjakarta.api.security;

import com.example.tpjakarta.services.TokenService;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class TokenLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private TokenService tokenService;

    private boolean succeeded = false;
    private boolean commitSucceeded = false;

    private String token;
    private UserPrincipal userPrincipal;
    private RolePrincipal rolePrincipal;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.tokenService = TokenService.getInstance(); // Or injection
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available");
        }

        Callback[] callbacks = new Callback[1];
        // Reuse NameCallback to pass token
        callbacks[0] = new NameCallback("token");

        try {
            callbackHandler.handle(callbacks);
            token = ((NameCallback) callbacks[0]).getName();

        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException(e.toString());
        }

        if (token == null) {
             throw new LoginException("Token is missing");
        }

        Long userId = tokenService.validateToken(token);
        if (userId != null) {
            this.userPrincipal = new UserPrincipal(userId.toString(), userId); // Name is userId as string
            succeeded = true;
            return true;
        } else {
            succeeded = false;
            throw new LoginException("Invalid or expired token");
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (!succeeded) {
            return false;
        } else {
            if (!subject.getPrincipals().contains(userPrincipal)) {
                subject.getPrincipals().add(userPrincipal);
            }
            
            rolePrincipal = new RolePrincipal("USER");
            if (!subject.getPrincipals().contains(rolePrincipal)) {
                subject.getPrincipals().add(rolePrincipal);
            }

            token = null;
            commitSucceeded = true;
            return true;
        }
    }

    @Override
    public boolean abort() throws LoginException {
         if (!succeeded) {
            return false;
        } else if (succeeded && !commitSucceeded) {
            succeeded = false;
            token = null;
            userPrincipal = null;
        } else {
            logout();
        }
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        subject.getPrincipals().remove(rolePrincipal);
        succeeded = false;
        commitSucceeded = false;
        token = null;
        userPrincipal = null;
        return true;
    }
}
