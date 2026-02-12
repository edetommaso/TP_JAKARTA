package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "userList", value = "/user-list")
public class UserList extends HttpServlet {

    UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<User> users = userRepository.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("WEB-INF/WebContent/UserList.jsp").forward(request, response);
    }
}
