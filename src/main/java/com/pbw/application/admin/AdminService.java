package com.pbw.application.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pbw.application.custom.CustomResponse;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(Admin admin) throws Exception {
        Optional<Admin> res = adminRepository.findByEmail(admin.getEmail());
        
        if(res.isPresent()){
            return false;
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));        

        adminRepository.save(admin);
        return true;
    }

    public CustomResponse<Admin> login(String Email, String password) {
        Optional<Admin> res = adminRepository.findByEmail(Email);
        
        if(!res.isPresent()){
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "NO USER FOUND" , null );
        }

        return passwordEncoder.matches(password, res.get().getPassword()) ? 
        
        new CustomResponse<Admin>(HttpStatus.ACCEPTED,true, "USER FOUND", res.get() ) : new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "PASSWORD MISMATCH" , null );
    }

    // Ga perlu custom response seharusnya, karena bakal disatuin di login????? 
    public int getIdUsersByEmail(String email){
        return adminRepository.getIdUsersByEmail(email);
    } 

    public boolean validatePassword (Admin user){
        return user.getPassword().equals(user.getConfirmpassword()) ? true : false;
    }
}