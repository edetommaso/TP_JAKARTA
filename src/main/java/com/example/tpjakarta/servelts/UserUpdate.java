package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "userUpdate", value = "/user-update")
public class UserUpdate extends HttpServlet {

    UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        User user = userRepository.findById(id);
        request.setAttribute("user", user);
        request.getRequestDispatcher("WEB-INF/WebContent/UserUpdate.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userRepository.findById(id);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            // Only update password if a new one is provided
            if (password != null && !password.isEmpty()) {
                user.setPassword(password); // In a real app, hash this!
            }
            userRepository.update(user);
        }
        response.sendRedirect("user-list");
    }
}
