package com.pbw.application.config.Login;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
        User user = (User) session.getAttribute("status");
        
        // If no user in session, redirect to login
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
    
    private boolean isPublicPath(String requestURI) {
        return PUBLIC_PATHS.stream()
            .anyMatch(path -> {
                if (path.endsWith("/**")) {
                    String basePath = path.substring(0, path.length() - 3);
                    return requestURI.startsWith(basePath);
                }
                return requestURI.equals(path);
            });
    }
}