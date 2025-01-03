package com.pbw.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityRepository;
import com.pbw.application.activity.ActivityService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.image.ImageService;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String getAllActivities(Model model) {
        List<Activity> activities = activityService.findAll();
        model.addAttribute("activities", activities);
        return "/activity/activity";
    }

    @GetMapping("/add")
    public String showAddActivityForm(
        Model model,
        HttpSession httpSession
    ) throws IOException {

        
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
                                @RequestParam float jarak,
                                @RequestParam(required = false) float elevasi,
                                @RequestParam(required = false) String description,
                                // @RequestParam(required = false) Integer id_runner,
                                @RequestParam("image") MultipartFile file,
                                HttpSession httpSession) throws IOException {

                        
        if(file.isEmpty()){
            // Sementara kalau ga ada input gambar di balikin ke halaman add
            return "redirect:/activity/add";
        }

        int id_runner = (int)httpSession.getAttribute("id_user");
        int nextIdActivity = activityService.getIdActivity() + 1;

        // tipe training
        CustomResponse<String> isAdded = imageService.addImage(file, id_runner, nextIdActivity,"T");
        
        if(!isAdded.isSuccess()){

            System.out.println(isAdded.getMessage());
        }

        // ====================================================
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

        // set url path dengan yang sebelumnya telah dibuat
        String path = imageService.getActualImagePath(id_runner, isAdded.getData());
        activity.setUrlpath(isAdded.getData());

        activity.setUrlpath(path);

        // System.out.println("Parameters: " + 
        // activity.getJudul() + ", " + 
        // activity.getTipeTraining() + ", " +
        // activity.getCreatedAt() + ", " +
        // activity.getDurasi() + ", " +
        // activity.getJarak() + ", " +
        // activity.getElevasi() + ", " +
        // activity.getDescription() + ", " +
        // activity.getIdRunner());

        activityService.save(activity);
        return "redirect:/activity";
    }
}
