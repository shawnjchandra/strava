package com.pbw.application.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pbw.application.custom.CustomResponse;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(User user) throws Exception {
        Optional<User> res = userRepository.findByEmail(user.getEmail());
        
        if(res.isPresent()){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));        

        userRepository.save(user);
        return true;
    }

    public CustomResponse<User> login(String Email, String password) {
        Optional<User> res = userRepository.findByEmail(Email);
        
        if(!res.isPresent()){
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "NO USER FOUND" , null );
        }

        return passwordEncoder.matches(password, res.get().getPassword()) ? 
        new CustomResponse<User>(HttpStatus.ACCEPTED,true, "USER FOUND", res.get() ) : new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "PASSWORD MISMATCH" , null );
    }

    public boolean validatePassword (User user){
        return user.getPassword().equals(user.getConfirmpassword()) ? true : false;
    }

    
}
