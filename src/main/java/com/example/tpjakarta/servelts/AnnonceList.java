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
import java.util.List;

@WebServlet(name = "annonceList", value = "/annonce-list")
public class AnnonceList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        try(Connection connection= ConnectionDB.getInstance()){
            AnnonceDAO annonceDAO = new AnnonceDAO(connection);

            List<Annonce> annonces = annonceDAO.findAll();

            System.out.println(annonces);
            request.setAttribute("annonces-front", annonces);

        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        request.getRequestDispatcher("WEB-INF/WebContent/AnnonceList.jsp").forward(request, response);

    }
}
