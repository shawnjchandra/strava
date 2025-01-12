package com.pbw.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pbw.application.RequiredRole;
import com.pbw.application.admin.Admin;
import com.pbw.application.admin.AdminService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.user.User;
import com.pbw.application.user.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AdminService adminService;


    @GetMapping("/login")
    public String getLoginView(
        HttpSession httpSession,
        Model model ){
            if(httpSession.getAttribute("status") != null){
                return "redirect:/dashboard";
            }
            
            // model.addAttribute("error", "test message");

        return "login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam("email") String email,    
        @RequestParam("password") String password,
        // BindingResult bindingResult,
        HttpSession httpSession,
        Model model 
    ){
        List<CustomResponse<Object>> errors = new ArrayList<>();

        if(email == null){
            errors.add(new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "EMAIL CAN'T BE EMPTY", null)) ;
        }
    
        if(password == null){
            errors.add(new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "PASSWORD CAN'T BE EMPTY", null));
        }
        

        CustomResponse<User> user = userService.login(email, password);


            // kalau user ga bisa, coba admin wkwkwkwk
        if(!user.isSuccess()){

            CustomResponse<Admin> admin = adminService.login(email, password);

            // //System.out.println(admin.isSuccess());

            if(admin.isSuccess()){
                int id_user = adminService.getIdUsersByEmail(email);
        
                httpSession.setAttribute("role", "admin");
                
                httpSession.setAttribute("status", admin.getData());
                httpSession.setAttribute("id_user", id_user);
                httpSession.setAttribute("email", email);
                
                return "redirect:/admin/index";
            }
            
            errors.add(new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, user.getMessage(), null));   
        }
        
        if(!errors.isEmpty()){
            StringBuilder message = new StringBuilder();
            
            errors.forEach((error)->{
                message.append(""+error.getMessage()+"\n");
            });
            
            model.addAttribute("error", message);
            return "login";
        }
        
        // Hanya di jalanin kalau 
        int id_user = userService.getIdUsersByEmail(email);
        
        
        httpSession.setAttribute("role", "runner");

        httpSession.setAttribute("status", user.getData());
        httpSession.setAttribute("id_user", id_user);
        httpSession.setAttribute("email", email);

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(
        HttpSession httpSession
    ){
        httpSession.invalidate();
        return "redirect:/";
    }


}
 