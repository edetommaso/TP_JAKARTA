package com.example.tpjakarta.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final Dotenv dotenv = Dotenv.load(); // Charge le .env par défaut

    // On utilise les noms de clés simplifiés du .env
    private String url = dotenv.get("DB_URL");
    private String user = dotenv.get("DB_USERNAME");
    private String passwd = dotenv.get("DB_PASSWORD");

    private static Connection connect;

    private ConnectionDB() throws ClassNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() throws ClassNotFoundException, SQLException {
        if (connect == null || connect.isClosed()) {
            new ConnectionDB();
        }
        return connect;
    }
}