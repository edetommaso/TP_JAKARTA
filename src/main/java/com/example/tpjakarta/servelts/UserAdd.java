package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(name = "userAdd", value = "/user-add")
public class UserAdd extends HttpServlet {

    UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("WEB-INF/WebContent/UserAdd.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (username != null && !username.isEmpty() && email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password); // In a real app, hash this!
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.create(user);
        }
        response.sendRedirect("user-list");
    }
}
