package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.repositories.CategoryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "categoryAdd", value = "/category-add")
public class CategoryAdd extends HttpServlet {

    CategoryRepository categoryRepository;

    public void init() {
        categoryRepository = new CategoryRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("WEB-INF/WebContent/CategoryAdd.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String label = request.getParameter("label");
        if (label != null && !label.isEmpty()) {
            Category category = new Category();
            category.setLabel(label);
            categoryRepository.create(category);
        }
        response.sendRedirect("category-list");
    }
}
