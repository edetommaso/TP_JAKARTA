package com.example.tpjakarta.servelts;

import com.example.tpjakarta.daos.AnnonceDAO;
import com.example.tpjakarta.utils.ConnectionDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "annonceDelete", value = "/annonce-delete")
public class AnnonceDelete extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));

        try (Connection connection = ConnectionDB.getInstance()) {
            AnnonceDAO annonceDAO = new AnnonceDAO(connection);
            annonceDAO.delete(id);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        response.sendRedirect("annonce-list");
    }
}
