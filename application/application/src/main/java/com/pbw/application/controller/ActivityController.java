package com.pbw.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityRepository;

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

    public static String UPLOAD_DIRECTORY;

    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping
    public String getAllActivities(Model model) {
        List<Activity> activities = activityRepository.findAll();
        model.addAttribute("activities", activities);
        return "/activity/activity";
    }

    @GetMapping("/add")
    public String showAddActivityForm(
        Model model,
        HttpSession httpSession
    ) throws IOException {

        int id = (int)httpSession.getAttribute("id_user");
        List<String> images = new ArrayList<>();

        UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads" + "/"+id;

            File dirs = new File(UPLOAD_DIRECTORY);

            if(!dirs.exists()){
                dirs.mkdirs();
                dirs.setExecutable(true,false);
                dirs.setWritable(true,false);
                dirs.setReadable(true,false);
            }
            Path directory = Paths.get(UPLOAD_DIRECTORY);

            try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)){
                for(Path file : stream){

                    String pathName = "/uploads/" + id + "/" + file.getFileName().toString();
                    images.add(pathName);

                }
            }
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
                                // @RequestParam(required = false) Integer id_runner,
                                @RequestParam("image") MultipartFile file,
                                HttpSession httpSession) throws IOException {

                        

        if(file.isEmpty()){
            // Sementara kalau ga ada input gambar di balikin ke halaman add
            return "redirect:/activity/add";
        }
      
        int id_runner = (int)httpSession.getAttribute("id_user");

        StringBuilder fileNames = new StringBuilder();
        
        // TODO: nanti harus ganti si nama filenya jadi sesuai si
        int nextIdActivity = activityRepository.getIdActivity() + 1;
        String pathName = nextIdActivity+"."+file.getOriginalFilename().split("\\.")[1];

        

        fileNames.append(pathName);

        
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, pathName);


        Files.write(fileNameAndPath, file.getBytes());
        File uploadedFile = fileNameAndPath.toFile();
        uploadedFile.setReadable(true,false);    
        uploadedFile.setWritable(true,false);    

        
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
