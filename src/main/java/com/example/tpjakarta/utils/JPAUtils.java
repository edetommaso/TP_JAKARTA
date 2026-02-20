package com.example.tpjakarta.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtils {
    private static EntityManagerFactory emf;
    private static String persistenceUnitName = "default";

    public static void setPersistenceUnitName(String name) {
        persistenceUnitName = name;
        close(); // Reset EMF to force recreation with new name
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            } catch (Exception e) {
                System.err.println(" Erreur initialisation (" + persistenceUnitName + ") : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }
}
