package com.pbw.application.controller;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.RequiredRole;
import com.pbw.application.activity.Activity;
import com.pbw.application.admin.Admin;
import com.pbw.application.admin.AdminService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.custom.PaginationResponse;
import com.pbw.application.hash.HashService;
import com.pbw.application.race.RaceService;
import com.pbw.application.user.User;
import com.pbw.application.user.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private RaceService raceService;

    @Autowired
    private HashService hashService;
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    @RequiredRole("admin")
    public String adminPage( 
        Admin admin,
        Model model    
    ){
        model.addAttribute("admin", admin);
        return "admin/adminRegistration";       
    }

    @GetMapping("/index")
    @RequiredRole("admin")
    public String adminIndexPage( 
        Model model,    
        HttpSession httpSession
    ) throws NoSuchAlgorithmException, IOException{
        Admin admin = (Admin) httpSession.getAttribute("status"); 

        List<User> users = userService.getAllUsers();

        List<Runner> runners = new ArrayList<>();
        for(User user: users){
           
            Runner runner = new Runner().userWithIdRunner(user);
            runners.add(runner);
        }

        CustomResponse<List<Runner>> runnersToWeb;
        if(runners.size() >0){
            runnersToWeb = new CustomResponse<List<Runner>>(true, "Found runners", runners);
        }else{
            runnersToWeb = new CustomResponse<List<Runner>>(false, "No runners available", null);

        }

        List<Activity> raceQuery =  raceService.getAllRace();
        CustomResponse<List<Activity>> raceResponse;
        if(raceQuery != null){
            
            
            for(Activity act : raceQuery){
                String originalId = String.valueOf(act.getIdActivity());
                String hashedId = hashService.hashIdToInt(originalId);
            
                act.setIdActivity(Integer.valueOf(hashedId));
            }
            raceResponse = new CustomResponse<>(true, "Found available races", raceQuery);
        }else{
            raceResponse = new CustomResponse<>(false, "No available races", null);

        }


        model.addAttribute("races", raceResponse);
        model.addAttribute("runners", runnersToWeb);
        model.addAttribute("admin", admin);
        return "admin/index";       
    }

     @GetMapping("/filter/race")
    public ResponseEntity<?> filterRace(
        @RequestParam(required = false) String raceName,
        @RequestParam(defaultValue = "judul") String sortBy,
        @RequestParam(defaultValue = "asc") String sortOrder,
        Model model,
        HttpSession httpSession
    ){
         try {
            List<Activity> raceQuery =  raceService.getAllRaceFiltered(raceName, sortBy, sortOrder);
            CustomResponse<List<Activity>> raceResponse;
            if(raceQuery != null){
            
            
            for(Activity act : raceQuery){
                String originalId = String.valueOf(act.getIdActivity());
                String hashedId = hashService.hashIdToInt(originalId);
            
                act.setIdActivity(Integer.valueOf(hashedId));
            }
            raceResponse = new CustomResponse<>(true, "Found available races", raceQuery);
        }else{
            raceResponse = new CustomResponse<>(false, "No available races", null);

        }

        return ResponseEntity.ok(raceResponse);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(new CustomResponse<Exception>(false, "Error: " + e.getMessage(), null));
    }
    }
     @GetMapping("/filter/runner")
    public ResponseEntity<?> filterRunner(
        @RequestParam(required = false) String runnerName,
        @RequestParam(defaultValue = "id_runner") String sortBy,
        @RequestParam(defaultValue = "asc") String sortOrder,
        Model model,
        HttpSession httpSession
    ){
         try {
            //System.out.println("runner name"+runnerName);

            List<User> users = userService.getAllUsersByFilter(runnerName,sortBy,sortOrder);

            List<Runner> runners = new ArrayList<>();
            for(User user: users){
               
                Runner runner = new Runner().userWithIdRunner(user);
                runners.add(runner);
            }
    
            CustomResponse<List<Runner>> runnersToWeb;
            if(runners.size() >0){
                runnersToWeb = new CustomResponse<>(true, "Found runners", runners);
            }else{
                runnersToWeb = new CustomResponse<>(false, "No runners available", null);
    
            }

        return ResponseEntity.ok(runnersToWeb);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(new CustomResponse<Exception>(false, "Error: " + e.getMessage(), null));
    }
    }

    @Getter
    @Setter
    class Runner{
        User user;
        int id_runner;
        boolean active;

        Runner userWithIdRunner(User user){
            Runner runner = new Runner();

            String email = user.getEmail();
            int id_runner = userService.getIdRunnerByEmail(email);
            boolean status = userService.getActiveStatus(email);

            runner.setUser(user);
            runner.setId_runner(id_runner);
            runner.setActive(status);

            return runner;
        }
    }

    @PostMapping("/register/admin")
    public String registerUser(
        @Valid @ModelAttribute("admin") Admin admin,
        BindingResult bindungResult,
        @RequestParam("confirmpassword") String confirmpassword,
    
        Model model) throws Exception{

            if(admin.getEmail() == null || admin.getEmail().length() < 4){
                bindungResult.addError(new ObjectError("emailError", "email error"));;
                bindungResult.rejectValue("email",
                 "emailNotAllowed",
                  "Email doesn't fulfilled the requirements");

            }
            if(admin.getPassword() == null || admin.getPassword().length() < 4){
                bindungResult.addError(new ObjectError("passError", "password error"));;
                bindungResult.rejectValue("password",
                 "passwordNotAllowed",
                  "password doesn't fulfilled the requirements");

            }



            if(!adminService.validatePassword(admin)){
                bindungResult.rejectValue("confirmpassword",
                 "passwordMismatch",
                  "Passwords do not match");

                  //System.out.println("rejected do not match");
                
                return "admin/adminRegistration";
                
            }
            //System.out.println("no reject password");
            
            if(bindungResult.hasErrors()){
                
                List<ObjectError> errors = bindungResult.getAllErrors();
                for(int i = 0; i<errors.size();i++){
                    //System.out.println("Error-"+i+": "+ errors.get(i));
                }
                return "admin/adminRegistration";
            }
            //System.out.println("no errors");
            
            
            
            
            boolean isSuccessful = adminService.register(admin);
            
            
            //System.out.println("services register success");
        
        
        if(isSuccessful){
            return "redirect:/login";
        }

        return "redirect:/admin";
    }

    @PostMapping("/ban/{id}")
    public ResponseEntity<String> ban(
        @PathVariable("id") String id
    ){
        int id_runner = Integer.valueOf(id);
        boolean result = userService.switchActiveStatusByIdRunner(id_runner,false);

        return result ? ResponseEntity.ok("User has been banned") :
        ResponseEntity.ok("Failed to ban user");
    
    }
    @PostMapping("/unban/{id}")
    public ResponseEntity<String> unban(
        @PathVariable("id") String id
    ){
        int id_runner = Integer.valueOf(id);
        boolean result = userService.switchActiveStatusByIdRunner(id_runner,true);

        return result ? ResponseEntity.ok("User has been banned") :
        ResponseEntity.ok("Failed to ban user");
    }

    

}
