package com.pbw.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pbw.application.RequiredRole;

@Controller
@RequestMapping("/race")
public class RaceController {
    
    @GetMapping("/")
    public String getRaceListView(){
        return "race/racelist";
    }

    @GetMapping("/create")
    // @RequiredRole("admin")
    public String getCreateRace(){
        return "race/racecreate";
    }

    @GetMapping("/challenge")
    // @RequiredRole("admin")
    public String getRaceChallenge(){
        return "race/challenge";
    }
}
