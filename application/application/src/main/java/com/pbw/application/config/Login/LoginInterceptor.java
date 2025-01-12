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
        "/maps",
        "/homeRun",
        "/homeCycle",
        "/homeSwim",
        "/homeFeature",
        "/homeChallenge",
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
        
        // kalau blm login atau tercatat di session, balikin ke login
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
    
    private boolean isPublicPath(String requestURI) {
        // cek kalau sama persis
        if (PUBLIC_PATHS.contains(requestURI)) {
            return true;
        }
        
    //    cek kalau ada kesamaan di folder childnya
        return PUBLIC_PATHS.stream()
            .anyMatch(path -> 
                requestURI.startsWith(path + "/") ||
                requestURI.startsWith(path + ".")    
            );
    }
}