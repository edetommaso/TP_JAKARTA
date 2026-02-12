package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "annonceDelete", value = "/annonce-delete")
public class AnnonceDelete extends HttpServlet {

    AnnonceRepository annonceRepository;

    public void init() {
        annonceRepository = new AnnonceRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        Annonce annonce = annonceRepository.findById(id);
        User user = (User) request.getSession().getAttribute("user");

        if (annonce != null && user != null && annonce.getAuthor().getId().equals(user.getId())) {
            annonceRepository.delete(id);
        }
        // Redirect whether deletion was successful or not to prevent showing an error
        // and to hide the fact that an ad with this ID exists.
        response.sendRedirect("annonce-list");
    }
}


