package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {

    UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userRepository.findByUsername(username);

        // For now, simple password check. In a real app, use a secure hash.
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
