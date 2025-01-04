package com.pbw.application.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pbw.application.RequiredRole;
import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.hash.HashService;
import com.pbw.application.race.RaceService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Null;

@Controller
@RequestMapping("/race")
public class RaceController {
    
    @Autowired
    private ActivityService activityService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private HashService hashService;

    @GetMapping("/")
    public String getRaceListView(
        Model model,
        HttpSession httpSession
    ) throws NoSuchAlgorithmException, IOException{

        
        int id_runner = (int)httpSession.getAttribute("id_user");

        CustomResponse<List<Activity>> available = raceService.getAllAvailableRace(id_runner);
        CustomResponse<List<Activity>> joined = raceService.getAllJoinedRace(id_runner);

        if(joined.isSuccess()){
            List<Activity> joined_list = joined.getData();
    
            for(Activity act : joined_list){
                String originalId = String.valueOf(act.getIdActivity());
                String hashedId = hashService.hashIdToInt(originalId);
            
                act.setIdActivity(Integer.valueOf(hashedId));
            }

        }

        // 
        model.addAttribute("available_obj", available);
        model.addAttribute("available", available.getData());

        model.addAttribute("joined_obj", joined);
        model.addAttribute("joined", joined.getData());

        return "race/racelist";
    }

    @GetMapping("/create")
    @RequiredRole("admin")
    public String getCreateRace(){

        return "race/create";
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

    @GetMapping("/challenge/{id}")
    // @RequiredRole("admin")
    public String getRaceChallenge(
        @PathVariable("id") String id,
        Model model,
        HttpSession httpSession
        ) throws NumberFormatException, IOException{
        

        int id_activity = Integer.valueOf(hashService.getIdByHashedId(id));

        Activity race = raceService.getActivityByIdActivity(id_activity);
        


        // TODO: nanti harus buat yang training nya sendiri
        List<Activity> activities = activityService.findAll();





        model.addAttribute("availableActivities", activities);
        model.addAttribute("race", race);

        return "race/challenge";
    }

    @PostMapping("/join/{id}")
    @ResponseBody
    public ResponseEntity<String> joinRace(
        @PathVariable("id") String id,
        HttpSession httpSession
        ){

        int id_runner = (int)httpSession.getAttribute("id_user");
        int id_activity = Integer.valueOf(id);

        CustomResponse<Null> result =raceService.joinRace(id_runner,id_activity);
        
        // int id_race = raceService.getIdRaceByIdActivity(id_activity);
        // System.out.println("id race : "+id_race);

        return ResponseEntity.ok(result.getMessage());
    }
}
