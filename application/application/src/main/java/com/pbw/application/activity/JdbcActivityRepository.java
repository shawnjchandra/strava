package com.pbw.application.activity;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
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

    // tipe training dan race disatuin
    private static final String FIND_ALL_QUERY = "SELECT * FROM Activity";

    // hanya untuk tipe training
    private static final String INSERT_QUERY = "INSERT INTO activity (judul, tipe_training, createdAt, durasi, jarak, elevasi, description, id_runner, urlpath) VALUES (?, ?::tipeAct, ?::date, ?, ?, ?, ?, ?,?)";

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
            activity.getIdRunner(),
            activity.getUrlpath()
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
            
            // urlpath
            activity.setUrlpath(rs.getString("urlpath"));
            return activity;
        }
    }

    @Override
    public int getIdActivity() {
        String sql = "Select id_activity from activity ORDER BY id_activity DESC;";

        List<Integer> res = jdbcTemplate.query(sql, this::mapIdActivity);

        // kalau pertama masih kosong 0, return index nilai 0, tapi kalau bukan, return nilai pada index paling atas
        return res.size() == 0 ? 0 : res.get(0) ;
    }

    private int mapIdActivity(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_activity");
    }


    
}
