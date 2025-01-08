package com.pbw.application.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.RequiredRole;
import com.pbw.application.admin.Admin;
import com.pbw.application.admin.AdminService;
import com.pbw.application.user.User;


import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/register")
    // @RequiredRole("admin")
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
        Admin admin,
        Model model    
    ){
        model.addAttribute("admin", admin);
        return "admin/index";       
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

                  System.out.println("rejected do not match");
                
                return "admin/adminRegistration";
                
            }
            System.out.println("no reject password");
            
            if(bindungResult.hasErrors()){
                
                List<ObjectError> errors = bindungResult.getAllErrors();
                for(int i = 0; i<errors.size();i++){
                    System.out.println("Error-"+i+": "+ errors.get(i));
                }
                return "admin/adminRegistration";
            }
            System.out.println("no errors");
            
            
            
            
            boolean isSuccessful = adminService.register(admin);
            
            
            System.out.println("services register success");
        
        
        if(isSuccessful){
            return "redirect:/login";
        }

        return "redirect:/admin";
    }

    

}