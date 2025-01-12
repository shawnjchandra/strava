package com.pbw.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.custom.PaginationResponse;
// import com.pbw.application.custom.CustomWrapper;
import com.pbw.application.image.ImageService;
import com.pbw.application.user.UserService;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String getAllActivities(
        @RequestParam(defaultValue = "0") int page,

        Model model,
        HttpSession httpSession
        ) {
        int id_user = (int)httpSession.getAttribute("id_user");

        List<Activity> act = activityService.findAllByIdUser(id_user);

        // //System.out.println("id_user: "+ id_user);
        // //System.out.println("activities: "+ act);

        CustomResponse<List<Activity>> activities;
        if(act != null&&act.size()>0){
            activities = new CustomResponse<>(true,"Found Activities", act);
        }else{
            activities = new CustomResponse<>(false,"No Activities Available",null);
        }



        model.addAttribute("currentPage", page);
        model.addAttribute("activities", activities);
        return "/activity/activity";
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterActivities(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keywords,
        @RequestParam(required = false) String actType,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String sortOrder,
        Model model,
        HttpSession httpSession
    ){
         try {
        int id_user = (int)httpSession.getAttribute("id_user");
        List<Activity> act = activityService.findAllFilteredTraining(id_user, keywords, actType, sortBy, sortOrder);

        PaginationResponse<List<Activity>> activities;
        CustomResponse<PaginationResponse<List<Activity>>> response;

        if(act != null && !act.isEmpty()) {
            activities = new PaginationResponse<>(true, act, page, 5, act.size());

            response = new CustomResponse<>(
            true, 
            "",
            activities
            );
        } 
        else {
        
            response = new CustomResponse<>(false, "No Activities Available", null);
        }
        

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(new CustomResponse<Exception>(false, "Error: " + e.getMessage(), null));
    }
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
                                @RequestParam(required =  false) MultipartFile image,
                                HttpSession httpSession) throws IOException {


        int id_user = (int)httpSession.getAttribute("id_user");
        int id_runner = userService.getIdRunnerByIdUsers(id_user);
        int nextIdActivity = activityService.getIdActivity() + 1;

        // tipe training

        

        // CustomResponse<String> isAdded = imageService.addImage(image, id_runner, nextIdActivity,"T");
        

        // ====================================================
        // Format durasi sebagai hh:mm:ss
        String durasi = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        // Buat objek Activity
        Activity activity = new Activity();

        activity.setJudul(judul);
        activity.setTipeTraining(tipe_training);
        activity.setCreatedAt(LocalDateTime.parse(createdAt + "T00:00:00"));
        activity.setDurasi(durasi);
        activity.setJarak(jarak);
        activity.setElevasi(elevasi);
        activity.setDescription(description);
        activity.setIdRunner(id_runner);

        if (image != null && !image.isEmpty()) {
            CustomResponse<String> isAdded = imageService.addImage(image, id_runner, nextIdActivity, "T");
            String path = imageService.getActualImagePath(id_runner, isAdded.getData());
            activity.setUrlpath(path);


        }

        // set url path dengan yang sebelumnya telah dibuat
       

        activityService.save(activity);
        return "redirect:/activity";
    }

    @GetMapping("/descActivity")
    public String getActivityDetails(@RequestParam("id_activity") int idActivity, HttpSession session, Model model) {
        Integer idUser = (Integer) session.getAttribute("id_user");
        if (idUser == null) {
            return "redirect:/login"; // Redirect ke login jika session tidak ditemukan
        }

        // Ambil id_runner dari id_user
        int idRunner = activityService.getIdRunnerByIdUser(idUser);

        // Cari activity berdasarkan id_activity dan id_runner
        Activity activity = activityService.getActivityByIdAndRunner(idActivity, idRunner);

        if (activity == null) {
            model.addAttribute("message", "Activity not found or you don't have permission to access it.");
            return "error";
        }

        model.addAttribute("activity", activity);
        return "/activity/descActivity"; // Tampilkan halaman detail
    }
}
