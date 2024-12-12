package com.pbw.application.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void save(User user) throws Exception{
        String sql = "INSERT INTO runners (username, password, nama, tanggal_lahir, lokasi, gender) VALUES (?,?,?,?::date,?,?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getNama(), user.getTanggal_lahir(), user.getLokasi(), user.getGender());
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM runners WHERE username = ?";
        List<User> results = jdbcTemplate.query(sql, this::mapRowToUser, username);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("password"),
            resultSet.getString("nama"),
            resultSet.getString("tanggal_lahir"),
            resultSet.getString("lokasi"),
            resultSet.getString("gender")
        );
    }

}
