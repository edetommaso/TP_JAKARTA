package com.example.tpjakarta.servelts;

import com.example.tpjakarta.repositories.CategoryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "categoryDelete", value = "/category-delete")
public class CategoryDelete extends HttpServlet {

    CategoryRepository categoryRepository;

    public void init() {
        categoryRepository = new CategoryRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        categoryRepository.delete(id);
        response.sendRedirect("category-list");
    }
}
