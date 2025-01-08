package com.pbw.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.pbw.application.user.User;
import com.pbw.application.user.UserRepository;

@Controller
public class RunnerController {
    
    // @GetMapping("/add")
    // public String manualView(){
    //     return "/activity/add";
    // }

    // @GetMapping("/activity")
    // public String activityView(){
    //     return "/activity/activity";
    // }
}