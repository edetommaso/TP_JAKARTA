package com.example.tpjakarta.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HeureDepartPreprocessor {

    public static void convertPreprocessor(Path input, Path output) {
        try {
            List<String> lines = Files.readAllLines(input);
            if (lines.isEmpty())
                return;

            List<String> newLines = new ArrayList<>();
            String header = lines.get(0);
            // On ajoute la nouvelle colonne heure_decimal à la fin
            newLines.add(header + ",heure_decimal");

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty())
                    continue;

                String[] columns = line.split(",");
                // L'index de heure_depart est 1 d'après le CSV :
                // id_course,heure_depart,distance_km,pluie,jour_semaine,vehicule_type,retard
                String heureDepart = columns[1];
                double decimalHour = convertToDecimal(heureDepart);
                newLines.add(line + "," + decimalHour);
            }

            Files.write(output, newLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double convertToDecimal(String time) {
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours + (minutes / 60.0);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
