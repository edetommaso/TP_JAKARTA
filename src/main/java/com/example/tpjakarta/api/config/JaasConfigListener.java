package com.example.tpjakarta.api.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.net.URL;

@WebListener
public class JaasConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Set the JAAS config system property if not already set
        if (System.getProperty("java.security.auth.login.config") == null) {
            URL resource = getClass().getClassLoader().getResource("jaas.conf");
            if (resource != null) {
                String path = resource.getFile();
                // Fix for Windows paths if needed, though usually getFile works ok or needs decoding
                System.setProperty("java.security.auth.login.config", path);
                System.out.println("JAAS Config loaded from: " + path);
            } else {
                System.err.println("WARNING: jaas.conf not found in classpath!");
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to do
    }
}
