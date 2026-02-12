package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.repositories.CategoryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "categoryUpdate", value = "/category-update")
public class CategoryUpdate extends HttpServlet {

    CategoryRepository categoryRepository;

    public void init() {
        categoryRepository = new CategoryRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Category category = categoryRepository.findById(id);
        request.setAttribute("category", category);
        request.getRequestDispatcher("WEB-INF/WebContent/CategoryUpdate.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String label = request.getParameter("label");

        Category category = categoryRepository.findById(id);
        if (category != null && label != null && !label.isEmpty()) {
            category.setLabel(label);
            categoryRepository.update(category);
        }
        response.sendRedirect("category-list");
    }
}
