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
import com.pbw.application.user.User;
import com.pbw.application.user.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String homepageView(){
        return "index";
    }

    @GetMapping("/login")
    public String getLoginView(
        HttpSession httpSession,
        Model model ){
            if(httpSession.getAttribute("status") != null){
                return "dashboard";
            }

        return "login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam("username") String username,    
        @RequestParam("password") String password,
        HttpSession httpSession,
        Model model 
    ){
        User user = userService.login(username, password);
            
        if(user==null){

            return "/login";
        }
        
        httpSession.setAttribute("status", user);
        httpSession.setAttribute("username", username);

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    @RequiredRole({"*"})
    public String getDashboardView(){
        return "/dashboard";
    }
}
