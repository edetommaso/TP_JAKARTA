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

@WebServlet(name = "register", value = "/register")
public class RegisterServlet extends HttpServlet {

    UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // --- Validation ---
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (userRepository.findByUsername(username) != null) {
            request.setAttribute("error", "Ce nom d'utilisateur est déjà pris.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (userRepository.findByEmail(email) != null) {
            request.setAttribute("error", "Cette adresse email est déjà utilisée.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        // --- End Validation ---


        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // In a real app, hash this!
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.create(user);

        // Redirect to login with a success message
        response.sendRedirect("login?success=true");
    }
}
