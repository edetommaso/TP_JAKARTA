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

@WebServlet(name = "annonceAdd", value = "/annonce-add")
public class AnnonceAdd extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        request.getRequestDispatcher("WEB-INF/WebContent/AnnonceAdd.jsp").forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        Annonce annonce = new Annonce();

        annonce.setTitle(request.getParameter("title"));
        annonce.setDescription(request.getParameter("description"));
        annonce.setAdress(request.getParameter("adress"));
        annonce.setMail(request.getParameter("mail"));
        annonce.setDate(new Timestamp(System.currentTimeMillis()));

        try(Connection connection= ConnectionDB.getInstance()){
            AnnonceDAO annonceDAO = new AnnonceDAO(connection);

            annonceDAO.create(annonce);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("annonce-list");

    }

}
