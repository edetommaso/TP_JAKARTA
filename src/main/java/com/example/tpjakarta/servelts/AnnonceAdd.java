package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.repositories.CategoryRepository;
import com.example.tpjakarta.utils.AnnonceStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "annonceAdd", value = "/annonce-add")
public class AnnonceAdd extends HttpServlet {

    AnnonceRepository annonceRepository;
    CategoryRepository categoryRepository;

    public void init() {
        annonceRepository = new AnnonceRepository();
        categoryRepository = new CategoryRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Category> categories = categoryRepository.findAll();
        request.setAttribute("categories", categories);
        // Only allow creating as DRAFT or PUBLISHED
        request.setAttribute("statuses", List.of(AnnonceStatus.DRAFT, AnnonceStatus.PUBLISHED));
        request.getRequestDispatcher("WEB-INF/WebContent/AnnonceAdd.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User author = (User) request.getSession().getAttribute("user");
        if (author == null) {
            response.sendRedirect("login");
            return;
        }

        Annonce annonce = new Annonce();
        annonce.setTitle(request.getParameter("title"));
        annonce.setDescription(request.getParameter("description"));
        annonce.setAdress(request.getParameter("adress"));
        annonce.setMail(request.getParameter("mail"));
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setAuthor(author);

        // Set status from form
        String statusStr = request.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            annonce.setStatus(AnnonceStatus.valueOf(statusStr));
        } else {
            annonce.setStatus(AnnonceStatus.DRAFT); // Default to DRAFT
        }

        String categoryIdStr = request.getParameter("category");
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            Category category = categoryRepository.findById(Long.parseLong(categoryIdStr));
            annonce.setCategory(category);
        }

        annonceRepository.create(annonce);

        response.sendRedirect("annonce-list");
    }
}


