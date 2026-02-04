package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Annonce;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "annonceUpdate", value = "/annonce-update")
public class AnnonceUpdate extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try (Connection connection = ConnectionDB.getInstance()) {
            AnnonceDAO annonceDAO = new AnnonceDAO(connection);
            Annonce annonce = annonceDAO.findById(id);
            request.setAttribute("annonce", annonce);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/WebContent/AnnonceUpdate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("email");

        try (Connection connection = ConnectionDB.getInstance()) {
            AnnonceDAO annonceDAO = new AnnonceDAO(connection);
            Annonce annonce = annonceDAO.findById(id);
            if (annonce != null) {
                annonce.setTitle(title);
                annonce.setDescription(description);
                annonce.setAdress(adress);
                annonce.setMail(mail);
                annonce.setDate(Timestamp.valueOf(LocalDateTime.now()));
                annonceDAO.update(annonce);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        response.sendRedirect("annonce-list");
    }
}
