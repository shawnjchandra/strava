package com.pbw.application.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.RequiredRole;
import com.pbw.application.activity.Activity;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.race.RaceService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Null;

@Controller
@RequestMapping("/race")
public class RaceController {
    
    @Autowired
    private RaceService raceService;

    @GetMapping("/")
    public String getRaceListView(
        Model model,
        HttpSession httpSession
    ){

        
        int id_runner = (int)httpSession.getAttribute("id_user");

        CustomResponse<List<Activity>> available = raceService.getAllAvailableRace(id_runner);
        CustomResponse<List<Activity>> joined = raceService.getAllJoinedRace(id_runner);

        // System.out.println("List available:" +available.getData());
        

        // 
        model.addAttribute("available", available.getData());
        model.addAttribute("available_obj", available);
        model.addAttribute("joined", joined.getData());
        model.addAttribute("joined_obj", joined);

        return "race/racelist";
    }

    @GetMapping("/create")
    // @RequiredRole("admin")
    public String getCreateRace(){
        return "race/racecreate";
    }

    @PostMapping("/add")
    public String addRace(
        @RequestParam String judul,
        @RequestParam String tipe_race,
        @RequestParam String createdAt,
        @RequestParam int days,
        @RequestParam int hours,
        @RequestParam int minutes,
        @RequestParam int kuota,
        @RequestParam float jarak,
        @RequestParam(required = false) String description,
        Model model,
        HttpSession httpSession
    ){


        // Hard code dulu
        int id_admin = 1;

        // int id_dmin = (int)httpSession.getAttribute("admin").getId_Admin();


        String durasi = String.format("%d:%02d:%02d", days, hours, minutes);


        Activity act = new Activity();
        act.setJudul(judul);
        act.setTipeRace(tipe_race);
        act.setCreatedAt(LocalDate.parse(createdAt));
        act.setDurasi(LocalTime.parse(durasi));
        act.setKuota_max(kuota);
        act.setJarak(jarak);
        act.setDescription(description);

        CustomResponse<Null> res= raceService.addRace(act, id_admin);

        if(res.isSuccess()){
            return "redirect:/race/";
        }else{
            return "redirect:/race/create";
        }
        
    }

    @GetMapping("/challenge")
    // @RequiredRole("admin")
    public String getRaceChallenge(){
        return "race/challenge";
    }
}
