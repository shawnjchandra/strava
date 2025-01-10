package com.pbw.application.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.pbw.application.RequiredRole;
import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.image.ImageService;
import com.pbw.application.user.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ImageService imageService;

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
        int id_user = (int)httpSession.getAttribute("id_user");

        List<Activity> act = activityService.findAllByIdUser(id_user);

        // System.out.println(act);
        
        //Tipe T untuk menampilkan yang training 
        // Path directory = imageService.getDirectory(id);
        // List<String> images = imageService.getAllImages(directory, id);

        CustomResponse<List<Activity>> activities;
        if(act != null && act.size()>0){
            activities = new CustomResponse<>(true,"Found Activities", act);
        }else{
            activities = new CustomResponse<>(false,"No Activities Available",null);
        }

        model.addAttribute("nama", nama);

        // model.addAttribute("images", images);
        model.addAttribute("activities", activities);
        return "/dashboard";
      

    }

    @GetMapping("/maps")
    public String showMap(
        Model model,
        HttpSession httpSession
    ){
        return "mapmap";
    }
    
    @GetMapping("/homeRun")
    public String runActivity(){
        return "homeActivity";
    }
    @GetMapping("/homeCycle")
    public String dashActivity(){
        return "cycActivity";
    }
    @GetMapping("/homeSwim")
    public String swimActivity(){
        return "swimActivity";
    }
    @GetMapping("/homeFeature")
    public String homeFeature(){
        return "homeFeature";
    }
    @GetMapping("/homeChallenge")
    public String homeChallenge(){
        return "homeChallenge";
    }

}
