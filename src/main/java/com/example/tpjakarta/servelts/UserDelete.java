package com.example.tpjakarta.servelts;

import com.example.tpjakarta.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "userDelete", value = "/user-delete")
public class UserDelete extends HttpServlet {

    UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = Long.parseLong(request.getParameter("id"));
        userRepository.delete(id);
        response.sendRedirect("user-list");
    }
}
