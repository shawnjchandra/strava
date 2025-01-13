package com.pbw.application.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "No user has been found" , null );
        }

        boolean active = userRepository.getActiveStatus(Email);
        if(!active){
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "User has been banned" , null );
        }

        return passwordEncoder.matches(password, res.get().getPassword()) ? 
        new CustomResponse<User>(HttpStatus.ACCEPTED,true, "USER FOUND", res.get() ) : new CustomResponse<>(HttpStatus.UNAUTHORIZED,false, "Password mismatch" , null );
    }

    // Ga perlu custom response seharusnya, karena bakal disatuin di login????? 
    public int getIdUsersByEmail(String email){
        return userRepository.getIdUsersByEmail(email);
    } 

    public boolean validatePassword (User user){
        return user.getPassword().equals(user.getConfirmpassword()) ? true : false;
    }

    public User getUserByIdRunner(int id_runner){
        return userRepository.getUserByIdRunner(id_runner);
    }
  
    public int getIdRunnerByIdUsers(int id_users){
        return userRepository.getIdRunnerByIdUsers(id_users);
    }

    public List<String> getAllKota(){
        return userRepository.getAllKota();
    }

    
    //test 
    public List<User> getAllUsers() {
        
        List<User> result = userRepository.getAllUsers();

        return result;
    }

    public List<User> getAllUsersByFilter(String nama, String sortBy, String sortOrder) {
    
        
        List<User> result = userRepository.getAllUsersByFilter(nama, sortBy, sortOrder);
        return result != null && result.size() > 0 ? result : new ArrayList<>();
 
    }

    public int getIdRunnerByEmail(String email){
        return userRepository.getIdRunnerByEmail(email);
    }

    public boolean getActiveStatus(String email){
        return userRepository.getActiveStatus(email);
    }
    
    public boolean getActiveStatus(int id_runner){
        return userRepository.getActiveStatus(id_runner);
    }

    public boolean switchActiveStatusByIdRunner(int id_runner, boolean status){
        return userRepository.switchActiveStatusByIdRunner(id_runner, status);
    }

}
