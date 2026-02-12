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

import java.util.List;



@WebServlet(name = "annonceList", value = "/annonce-list")

public class AnnonceList extends HttpServlet {



    AnnonceRepository annonceRepository;

    CategoryRepository categoryRepository;



    public void init() {

        annonceRepository = new AnnonceRepository();

        categoryRepository = new CategoryRepository();

    }



        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {



            String keyword = request.getParameter("keyword");



            String categoryIdStr = request.getParameter("category");



            String statusStr = request.getParameter("status");



            String pageStr = request.getParameter("page");



            User currentUser = (User) request.getSession().getAttribute("user");



    



            int page = (pageStr == null) ? 1 : Integer.parseInt(pageStr);



            int pageSize = 10; // Or get from config



    



            Category category = null;



            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {



                category = categoryRepository.findById(Long.parseLong(categoryIdStr));



            }



    



            AnnonceStatus status = null;



            if (statusStr != null && !statusStr.isEmpty()) {



                try {



                    status = AnnonceStatus.valueOf(statusStr);



                } catch (IllegalArgumentException e) {



                    // Ignore invalid status parameter



                }



            }



    



            List<Annonce> annonces = annonceRepository.search(keyword, category, status, currentUser, page, pageSize);



            long totalAnnonces = annonceRepository.countSearch(keyword, category, status, currentUser);



            long totalPages = (long) Math.ceil((double) totalAnnonces / pageSize);



    



            List<Category> categories = categoryRepository.findAll();



            List<AnnonceStatus> statuses = List.of(AnnonceStatus.values());



    



            request.setAttribute("annoncesFront", annonces);



            request.setAttribute("categories", categories);



            // We still pass statuses for the filter, but the repository logic will handle visibility



            request.setAttribute("statuses", statuses);



            request.setAttribute("totalPages", totalPages);



            request.setAttribute("currentPage", page);



            request.setAttribute("keyword", keyword);



            request.setAttribute("categoryId", categoryIdStr);



            request.setAttribute("status", statusStr);



    



            request.getRequestDispatcher("WEB-INF/WebContent/AnnonceList.jsp").forward(request, response);



        }

}


