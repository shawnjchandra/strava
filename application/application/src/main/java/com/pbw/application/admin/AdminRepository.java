package com.pbw.application.admin;

import java.util.Optional;

public interface AdminRepository {
    void save(Admin user) throws Exception;
    Optional<Admin> findByEmail(String Email);
    int getIdUsersByEmail(String email);
}