package com.pbw.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping
    public String getAllActivities(Model model) {
        List<Activity> activities = activityRepository.findAll();
        model.addAttribute("activities", activities);
        return "/activity/activity";
    }

    @GetMapping("/add")
    public String showAddActivityForm() {
        return "/activity/add";
    }

    @PostMapping("/add")
    public String addActivity(
                                @RequestParam String judul,
                                @RequestParam String tipe_training,
                                @RequestParam String createdAt,
                                @RequestParam int hours,
                                @RequestParam int minutes,
                                @RequestParam int seconds,
                                @RequestParam int jarak,
                                @RequestParam(required = false) Integer elevasi,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) Integer id_runner) {

        if (id_runner == null) {
            id_runner = 1;
        }

        // Format durasi sebagai hh:mm:ss
        String durasi = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        // Buat objek Activity
        Activity activity = new Activity();

        activity.setJudul(judul);
        activity.setTipeTraining(tipe_training);
        activity.setCreatedAt(LocalDate.parse(createdAt));
        activity.setDurasi(LocalTime.parse(durasi));
        activity.setJarak(jarak);
        activity.setElevasi(elevasi);
        activity.setDescription(description);
        activity.setIdRunner(id_runner);

        System.out.println("Parameters: " + 
        activity.getJudul() + ", " + 
        activity.getTipeTraining() + ", " +
        activity.getCreatedAt() + ", " +
        activity.getDurasi() + ", " +
        activity.getJarak() + ", " +
        activity.getElevasi() + ", " +
        activity.getDescription() + ", " +
        activity.getIdRunner());

        activityRepository.save(activity);
        return "redirect:/activity";
    }
}
