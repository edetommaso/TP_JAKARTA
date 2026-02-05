package com.example.tpjakarta.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JPAInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(" Initialisation de JPA");
        JPAUtils.getEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtils.close();
    }
}
