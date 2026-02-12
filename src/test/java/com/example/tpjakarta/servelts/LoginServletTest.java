package com.example.tpjakarta.servelts;

import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.repositories.UserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;

    private LoginServlet loginServlet;

    @BeforeEach
    void setUp() {
        loginServlet = new LoginServlet();
        loginServlet.init(); // The real init method uses a real repository, so we must override it
        loginServlet.userRepository = userRepository;
    }

    @Test
    void doPost_Success_WhenCredentialsAreValid() throws ServletException, IOException {
        // Arrange
        User validUser = new User();
        validUser.setUsername("testuser");
        validUser.setPassword("password");

        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password");
        when(userRepository.findByUsername("testuser")).thenReturn(validUser);
        when(request.getSession()).thenReturn(session);

        // Act
        loginServlet.doPost(request, response);

        // Assert
        verify(session).setAttribute("user", validUser);
        verify(response).sendRedirect("index.jsp");
        verify(dispatcher, never()).forward(any(), any());
    }

    @Test
    void doPost_Failure_WhenCredentialsAreInvalid() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(userRepository.findByUsername("wronguser")).thenReturn(null);
        when(request.getRequestDispatcher("login.jsp")).thenReturn(dispatcher);

        // Act
        loginServlet.doPost(request, response);

        // Assert
        verify(request).setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
        verify(dispatcher).forward(request, response);
        verify(session, never()).setAttribute(anyString(), any());
        verify(response, never()).sendRedirect(anyString());
    }
}
