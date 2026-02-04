package com.example.tpjakarta.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionDB {
    private String url = "jdbc:postgresql://localhost:5432/MasterAnnonce";
    private String user = "postgres";
    private String passwd = "postgres";
    /**
     * Objet Connection
     */
    private static Connection connect;
    /**
     * Constructeur privé
     * @throws ClassNotFoundException
     */
    private ConnectionDB() throws ClassNotFoundException{
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Methode qui va nous retourner notre instance
     * et la creer si elle n'existe pas...
     * @return
     * @throws ClassNotFoundException
     */
    public static Connection getInstance() throws ClassNotFoundException , SQLException{
        if(connect == null || connect.isClosed()){
            new ConnectionDB();
        }
        return connect;
    }
}
