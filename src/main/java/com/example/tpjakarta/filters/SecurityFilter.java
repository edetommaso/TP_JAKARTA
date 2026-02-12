package com.example.tpjakarta.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    private final List<String> allowedExtensions = Arrays.asList(".css", ".js", ".jpg", ".jpeg", ".png", ".gif");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean isAllowedPath = path.equals("/login") || path.equals("/register") || path.equals("/logout");
        boolean isStaticResource = allowedExtensions.stream().anyMatch(path::endsWith);

        if (loggedIn || isAllowedPath || isStaticResource) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
}

