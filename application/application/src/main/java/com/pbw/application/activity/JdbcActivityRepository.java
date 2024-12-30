package com.pbw.application.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcActivityRepository implements ActivityRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_QUERY = "SELECT * FROM Activity";
    private static final String INSERT_QUERY = "INSERT INTO activity (judul, tipe_training, createdAt, durasi, jarak, elevasi, description, id_runner) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<Activity> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new ActivityRowMapper());
    }

    @Override
    public void save(Activity activity) {
        jdbcTemplate.update(INSERT_QUERY, 
            activity.getJudul(),
            activity.getTipeTraining(),
            activity.getCreatedAt(),
            activity.getDurasi(),
            activity.getJarak(),
            activity.getElevasi(),
            activity.getDescription(),
            activity.getIdRunner()
        );
    }

    private static class ActivityRowMapper implements RowMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            Activity activity = new Activity();
            activity.setIdActivity(rs.getInt("id_activity"));
            activity.setJudul(rs.getString("judul"));
            activity.setTipeTraining(rs.getString("tipe_training"));
            activity.setCreatedAt(rs.getDate("createdAt").toLocalDate());
            activity.setDurasi(rs.getTime("durasi").toLocalTime());
            activity.setJarak(rs.getInt("jarak"));
            activity.setElevasi(rs.getInt("elevasi"));
            activity.setDescription(rs.getString("description"));
            activity.setIdRunner(rs.getInt("id_runner"));
            return activity;
        }
    }
}
