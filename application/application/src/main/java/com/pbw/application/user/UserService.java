package com.pbw.application.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User login(String Email, String password) {
        Optional<User> res = userRepository.findByEmail(Email);
        

        return passwordEncoder.matches(password, res.get().getPassword()) ? res.get() : null;
    }

    public boolean validatePassword (User user){
        return user.getPassword().equals(user.getConfirmpassword()) ? true : false;
    }

    
}
