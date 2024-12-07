package com.pbw.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String homepageView(){
        return "index";
    }

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }
}
