package com.pbw.application.config.Login;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.pbw.application.admin.Admin;
import com.pbw.application.user.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    private final List<String> PUBLIC_PATHS = Arrays.asList(
        "/",
        "/login",
        "/register",
        "/maps",
        "/static/**",
        "/css/**",
        "/css/error/**",
        "/css/race/**",
        "/script/**",
        "/js/**",
        "/img/**"
            );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        String requestURI = request.getRequestURI();
        
        // Check if the path is public
        if (isPublicPath(requestURI)) {
            return true;
        }
        
        HttpSession session = request.getSession();

        Object user = session.getAttribute("status");
        
        // If no user in session, redirect to login
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
    
    private boolean isPublicPath(String requestURI) {
        // First check exact matches
        if (PUBLIC_PATHS.contains(requestURI)) {
            return true;
        }
        
        // Then check if the URI starts with any of our public paths
        return PUBLIC_PATHS.stream()
            .anyMatch(path -> 
                requestURI.startsWith(path + "/") || // Matches subdirectories
                requestURI.startsWith(path + ".")    // Matches files with extensions
            );
    }
}