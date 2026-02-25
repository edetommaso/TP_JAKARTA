package com.example.tpjakarta.utils;

import com.example.tpjakarta.beans.Annonce;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {

    private static final Set<String> validAnnonceSortFields = new HashSet<>();

    static {
        // Introspection: get all declared fields from Annonce class
        for (Field field : Annonce.class.getDeclaredFields()) {
            validAnnonceSortFields.add(field.getName());
        }
    }

    /**
     * Valide si le champ demandé pour le tri existe bien sur l'entité Annonce via introspection.
     * @param field le nom du champ
     * @return true si le champ existe
     */
    public static boolean isValidAnnonceSortField(String field) {
        if (field == null || field.trim().isEmpty()) return false;
        
        // Handling the case where someone asks for "author.username" (nested)
        // For simplicity and safety, we might restrict sort to direct fields,
        // or validate the first part of a nested path. 
        // We will just validate direct fields as required by the exercise introspecting Annonce.class
        String baseField = field;
        if (field.contains(".")) {
            baseField = field.split("\\.")[0];
        }

        return validAnnonceSortFields.contains(baseField);
    }
}
