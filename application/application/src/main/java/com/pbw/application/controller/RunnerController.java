package com.pbw.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RunnerController {
    
    @GetMapping("/add")
    public String manualView(){
        return "add";
    }

    @GetMapping("/activity")
    public String activityView(){
        return "activity";
    }
}
