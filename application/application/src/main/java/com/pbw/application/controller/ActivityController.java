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

        // System.out.println("id_user: "+ id_user);
        // System.out.println("activities: "+ act);

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

    private String getPaginationHtml(int currentPage, int totalItems, int itemsPerPage, String baseUrl) {
    Context context = new Context();
    context.setVariable("currentPage", currentPage);
    context.setVariable("totalItems", totalItems);
    context.setVariable("itemsPerPage", itemsPerPage);
    context.setVariable("baseUrl", baseUrl);
    
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    return templateEngine.process("layout/pagination", context);
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

        int id_user = (int)httpSession.getAttribute("id_user");
        int id_runner = userService.getIdRunnerByIdUsers(id_user);
        int nextIdActivity = activityService.getIdActivity() + 1;

        // tipe training
        CustomResponse<String> isAdded = imageService.addImage(file, id_runner, nextIdActivity,"T");
        
        // if(!isAdded.isSuccess()){

        //     System.out.println(isAdded.getMessage());
        // }

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
