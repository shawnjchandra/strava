package com.pbw.application.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    void save(User user) throws Exception;
    Optional<User> findByEmail(String Email);
    boolean getActiveStatus(String email);
    boolean getActiveStatus(int id_runner);

    int getIdUsersByEmail(String email);
    int getIdRunnerByEmail(String email);
    int getIdRunnerByIdUsers(int id_users);
    User getUserByIdRunner(int id_runner);
    
    List<String> getAllKota();

    public List<User> getAllUsersByFilter(Map<String,String> filterMap);
    public List<User> getAllUsers();

    boolean switchActiveStatusByIdRunner(int id_runner, boolean status);
} 