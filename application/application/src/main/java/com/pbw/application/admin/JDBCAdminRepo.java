package com.pbw.application.admin;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCAdminRepo implements AdminRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void save(Admin user) throws Exception{
        String sql = "INSERT INTO admins (email, password) VALUES (?,?)";

        jdbcTemplate.update(sql, user.getEmail(), user.getPassword());
    }

    public Optional<Admin> findByEmail(String Email) {
        String sql = "SELECT * FROM admins WHERE email = ?";

        List<Admin> results = jdbcTemplate.query(sql, this::mapRowToUser, Email);

        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public int getIdUsersByEmail(String email) {
        String sql = "Select id_users FROM admins WHERE email = ?";

        List<Integer> res = jdbcTemplate.query(sql, this::mapIdUserToUser, email);
    
        return res.size() == 0 ? -1 : res.get(0);
    }

    private Admin mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new Admin(
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("password")
        );
    }

    private int mapIdUserToUser(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_users");
    }

}