package com.pbw.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.RequiredRole;
import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityRepository;
import com.pbw.application.user.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    
    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping("/")
    public String homepageView(){
        return "index";
    }

    @GetMapping("/dashboard")
    public String getDashboardView(
        Model model,
        HttpSession httpSession
    ){
        String nama = ((User)httpSession.getAttribute("status")).getNama();

        List<Activity> activities = activityRepository.findAll();

        model.addAttribute("nama", nama);
        model.addAttribute("activities", activities);
        return "/dashboard";
    }

    @GetMapping("/maps")
    public String showMap(){
        return "mapmap";
    }
}
