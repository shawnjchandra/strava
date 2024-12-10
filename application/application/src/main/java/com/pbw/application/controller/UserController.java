package com.pbw.application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.user.User;
import com.pbw.application.user.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerView(User user, Model model ){
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindungResult,
        @RequestParam("confirmpassword") String confirmpassword,
    
        Model model) throws Exception{

            if(user.getUsername() == null || user.getUsername().length() < 4){
                bindungResult.rejectValue("username",
                 "usernameNotAllowed",
                  "Username doesn't fulfilled the requirements");

            }
            if(user.getPassword() == null || user.getPassword().length() < 4){
                bindungResult.rejectValue("password",
                 "passwordNotAllowed",
                  "password doesn't fulfilled the requirements");

            }
            if(user.getNama() == null || user.getNama().length() < 4){
                bindungResult.rejectValue("name",
                 "nameNotAllowed",
                  "Name doesn't fulfilled the requirements");

            }

            if(user.getTanggal_lahir() == null){
                bindungResult.rejectValue("tanggal_lahir", "tgl_lahirEmpty",
                "Tanggal Lahir can't be empty");
            }

            if(user.getLokasi() == null){
                bindungResult.rejectValue("lokasi", "lokasiEmpty",
                "Lokasi can't be empty");
            }

            if(user.getGender() == null){
                bindungResult.rejectValue("gender", "genderEmpty",
                "gender can't be empty");
            }



            if(!userService.validatePassword(user)){
                bindungResult.rejectValue("confirmpassword",
                 "passwordMismatch",
                  "Passwords do not match");

                  System.out.println("rejected do not match");
                
                return "register";
                
            }

            if(bindungResult.hasErrors()){
                return "register";
            }
            



        boolean isSuccessful = userService.register(user);
        
        
        
        
        if(isSuccessful){
            return "redirect:/login";
        }

        return "redirect:/register";
    }


}
