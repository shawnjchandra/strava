package com.pbw.application.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pbw.application.user.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class ImageController {
    public static String UPLOAD_DIRECTORY;

    @GetMapping("/uploadimage/{id_user}")
    public String displayUploadForm(
        @PathVariable("id_user") int id, 
        Model model,    
        HttpSession httpSession) throws IOException{
            // String nama = ((User)httpSession.getAttribute("status")).getNama();
            // int id_user = (int)httpSession.getAttribute("id_user");

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
            model.addAttribute("id", id);
            model.addAttribute("nama", images);
        return "imageupload/index";
    }

    @PostMapping("/upload")
    public String uploadImage(Model model,
        @RequestParam("image") MultipartFile file,
        HttpSession httpSession
        ) throws IOException{
        StringBuilder fileNames = new StringBuilder();

        fileNames.append(file.getOriginalFilename());


        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());


        Files.write(fileNameAndPath, file.getBytes());
        File uploadedFile = fileNameAndPath.toFile();
        uploadedFile.setReadable(true,false);    
        uploadedFile.setWritable(true,false);    

        
        // hardcode bentar, males ngulangin dari awal
        int id = (int)httpSession.getAttribute("id_user");

        return "redirect:/uploadimage/"+id;
    }
}