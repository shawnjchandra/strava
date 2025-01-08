package com.pbw.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.user.User;
import com.pbw.application.user.UserRepository;
import com.pbw.application.user.UserService;

import jakarta.validation.Valid;



@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerView(User user, Model model ){
        List<String> daftarkota = userService.getAllKota();

        model.addAttribute("daftarkota", daftarkota);
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindungResult,
        @RequestParam("confirmpassword") String confirmpassword,
    
        Model model) throws Exception{

            if(user.getEmail() == null || user.getEmail().length() < 4){
                bindungResult.addError(new ObjectError("emailError", "email error"));;
                bindungResult.rejectValue("email",
                 "emailNotAllowed",
                  "Email doesn't fulfilled the requirements");

            }
            if(user.getPassword() == null || user.getPassword().length() < 4){
                bindungResult.addError(new ObjectError("passError", "password error"));;
                bindungResult.rejectValue("password",
                 "passwordNotAllowed",
                  "password doesn't fulfilled the requirements");

            }
            if(user.getNama() == null || user.getNama().length() < 4){
                bindungResult.addError(new ObjectError("namaError", "nama error"));;
                bindungResult.rejectValue("nama",
                 "nameNotAllowed",
                  "Name doesn't fulfilled the requirements");

            }

            if(user.getTanggal_lahir() == null){
                bindungResult.addError(new ObjectError("tanggal_lahirError", "dob error"));;
                bindungResult.rejectValue("tanggal_lahir", "tgl_lahirEmpty",
                "Tanggal Lahir can't be empty");
            }

            if(user.getLokasi() == null){
                bindungResult.addError(new ObjectError("lokasiError", "lokasi error"));;
                bindungResult.rejectValue("lokasi", "lokasiEmpty",
                "Lokasi can't be empty");
            }

            if(user.getGender() == null){
                bindungResult.addError(new ObjectError("genderError", "gender error"));;
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
            System.out.println("no reject password");
            if(bindungResult.hasErrors()){
                
                List<ObjectError> errors = bindungResult.getAllErrors();
                for(int i = 0; i<errors.size();i++){
                    System.out.println("Error-"+i+": "+ errors.get(i));
                }
                return "register";
            }
            System.out.println("no errors");
            
            
            
            
            boolean isSuccessful = userService.register(user);
            
            
            System.out.println("services register success");
        
        
        if(isSuccessful){
            return "redirect:/login";
        }

        return "redirect:/register";
    }


}