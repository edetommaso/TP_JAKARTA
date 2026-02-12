package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.AnnonceRepository;
import com.example.tpjakarta.repositories.CategoryRepository;
import com.example.tpjakarta.repositories.UserRepository;
import com.example.tpjakarta.utils.AnnonceStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "annonceUpdate", value = "/annonce-update")
public class AnnonceUpdate extends HttpServlet {

    AnnonceRepository annonceRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public void init() {
        annonceRepository = new AnnonceRepository();
        categoryRepository = new CategoryRepository();
        userRepository = new UserRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Annonce annonce = annonceRepository.findById(id);
        User user = (User) request.getSession().getAttribute("user");

        if (annonce == null || user == null || !annonce.getAuthor().getId().equals(user.getId())) {
            response.sendRedirect("annonce-list");
            return;
        }

        List<Category> categories = categoryRepository.findAll();
        List<User> users = userRepository.findAll();
        List<AnnonceStatus> statuses = List.of(AnnonceStatus.values());


        request.setAttribute("annonce", annonce);
        request.setAttribute("categories", categories);
        request.setAttribute("users", users);
        request.setAttribute("statuses", statuses);


        request.getRequestDispatcher("WEB-INF/WebContent/AnnonceUpdate.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Annonce annonce = annonceRepository.findById(id);
        User user = (User) request.getSession().getAttribute("user");

        if (annonce == null || user == null || !annonce.getAuthor().getId().equals(user.getId())) {
            response.sendRedirect("annonce-list");
            return;
        }

        annonce.setTitle(request.getParameter("title"));
        annonce.setDescription(request.getParameter("description"));
        annonce.setAdress(request.getParameter("adress"));
        annonce.setMail(request.getParameter("mail"));
        annonce.setDate(new Timestamp(System.currentTimeMillis()));

        String categoryIdStr = request.getParameter("category");
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            Category category = categoryRepository.findById(Long.parseLong(categoryIdStr));
            annonce.setCategory(category);
        }

        // Keep the original author, do not allow changing it from the form
        // String userIdStr = request.getParameter("author");
        // if (userIdStr != null && !userIdStr.isEmpty()) {
        //     User author = userRepository.findById(Long.parseLong(userIdStr));
        //     annonce.setAuthor(author);
        // }

        String statusStr = request.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            annonce.setStatus(AnnonceStatus.valueOf(statusStr));
        }

        annonceRepository.update(annonce);

        response.sendRedirect("annonce-list");
    }
}

