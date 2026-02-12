package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.repositories.CategoryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "categoryList", value = "/category-list")
public class CategoryList extends HttpServlet {

    CategoryRepository categoryRepository;

    public void init() {
        categoryRepository = new CategoryRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Category> categories = categoryRepository.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("WEB-INF/WebContent/CategoryList.jsp").forward(request, response);
    }
}
