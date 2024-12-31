package com.pbw.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.RequiredRole;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String homepageView(){
        return "index";
    }

    @GetMapping("/dashboard")
    public String getDashboardView(){
        

        return "/dashboard";
    }

    @GetMapping("/maps")
    public String showMap(){
        return "mapmap";
    }
}
