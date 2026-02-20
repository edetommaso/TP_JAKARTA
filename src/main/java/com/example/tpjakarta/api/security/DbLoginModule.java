package com.example.tpjakarta.api.security;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class DbLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private UserRepository userRepository;
    
    // Auth status
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    
    // User credentials
    private String username;
    private char[] password;
    
    // User Principal to be added to Subject
    private UserPrincipal userPrincipal;
    private RolePrincipal rolePrincipal;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.userRepository = new UserRepository(); // Or injection if possible
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available to garner authentication information from the user");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("username");
        callbacks[1] = new PasswordCallback("password", false);

        try {
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
            if (tmpPassword == null) {
                tmpPassword = new char[0];
            }
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
            ((PasswordCallback) callbacks[1]).clearPassword();

        } catch (java.io.IOException | UnsupportedCallbackException ioe) {
            throw new LoginException(ioe.toString());
        }

        // Verify credentials
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(new String(password))) { // Simple check, in real world verify hash
             // Check hash
             if (true) { // Assuming cleartext for this TP or existing logic
                 this.userPrincipal = new UserPrincipal(user.getUsername(), user.getId());
                 succeeded = true;
                 return true;
             }
        }
        
        // Cleanup
        succeeded = false;
        username = null;
        for (int i = 0; i < password.length; i++) password[i] = ' ';
        password = null;
        throw new LoginException("Authentication failed: Invalid username or password");
    }

    @Override
    public boolean commit() throws LoginException {
        if (!succeeded) {
            return false;
        } else {
            // Add Principals to Subject
            userPrincipal = new UserPrincipal(username, userRepository.findByUsername(username).getId());
            if (!subject.getPrincipals().contains(userPrincipal)) {
                subject.getPrincipals().add(userPrincipal);
            }
            
            // Add Role
            rolePrincipal = new RolePrincipal("USER"); // Default role
            if (!subject.getPrincipals().contains(rolePrincipal)) {
                subject.getPrincipals().add(rolePrincipal);
            }

            username = null;
            for (int i = 0; i < password.length; i++) password[i] = ' ';
            password = null;
            
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
            username = null;
            if (password != null) {
                for (int i = 0; i < password.length; i++) password[i] = ' ';
                password = null;
            }
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
        username = null;
        if (password != null) {
            for (int i = 0; i < password.length; i++) password[i] = ' ';
            password = null;
        }
        userPrincipal = null;
        return true;
    }
}
