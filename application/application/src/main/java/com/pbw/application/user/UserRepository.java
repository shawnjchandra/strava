package com.pbw.application.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user) throws Exception;
    Optional<User> findByEmail(String Email);

    int getIdUsersByEmail(String email);

    User getUserByIdRunner(int id_runner);
    
    List<String> getAllKota();
} 