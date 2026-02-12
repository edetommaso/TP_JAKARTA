package com.example.tpjakarta.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpSession session;

    @InjectMocks
    private SecurityFilter securityFilter;

    @Test
    void doFilter_RedirectsToLogin_WhenNotLoggedInAndAccessingProtectedResource() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/tp_jakarta_war/annonce-list");
        when(request.getContextPath()).thenReturn("/tp_jakarta_war");
        when(request.getSession(false)).thenReturn(null); // No session exists

        // Act
        securityFilter.doFilter(request, response, filterChain);

        // Assert
        verify(response).sendRedirect("/tp_jakarta_war/login");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilter_AllowsAccess_WhenLoggedInAndAccessingProtectedResource() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/tp_jakarta_war/annonce-list");
        when(request.getContextPath()).thenReturn("/tp_jakarta_war");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Object()); // A user object exists in session

        // Act
        securityFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilter_AllowsAccess_ToLoginPageWhenNotLoggedIn() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/tp_jakarta_war/login");
        when(request.getContextPath()).thenReturn("/tp_jakarta_war");
        when(request.getSession(false)).thenReturn(null);

        // Act
        securityFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
