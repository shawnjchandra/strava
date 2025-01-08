package com.pbw.application.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void save(User user) throws Exception{
        String sql = "INSERT INTO runners (email, password, nama, tanggal_lahir, lokasi, gender) VALUES (?,?,?,?,?,?)";
        Date sqlDate = Date.valueOf(user.getTanggal_lahir());
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getNama(), sqlDate, user.getLokasi(), user.getGender());
    }

    public Optional<User> findByEmail(String Email) {
        String sql = "SELECT * FROM Runners WHERE email = ?";

        List<User> results = jdbcTemplate.query(sql, this::mapRowToUser, Email);

        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public int getIdUsersByEmail(String email) {
        String sql = "Select id_users FROM runners WHERE email = ?";

        List<Integer> res = jdbcTemplate.query(sql, this::mapIdUserToUser, email);
    
        return res.size() == 0 ? -1 : res.get(0);
    }

    @Override
    public User getUserByIdRunner(int id_runner) {
        String sql = "Select * FROM runners WHERE id_runner = ?";

        User runner = jdbcTemplate.queryForObject(sql, this::mapRowToUser,id_runner);
    
        return runner;
    }


    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("password"),
            resultSet.getString("nama"),
            resultSet.getDate("tanggal_lahir").toLocalDate(),
            resultSet.getString("lokasi"),
            resultSet.getString("gender")
        );
    }

    private int mapIdUserToUser(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_users");
    }

    @Override
    public List<String> getAllKota() {
        String sql = "Select nama from Kota";

        List<String> result = jdbcTemplate.query(sql, this::mapKota);

        return result;
    }
    private String mapKota(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getString("nama");
    }


   
    

}