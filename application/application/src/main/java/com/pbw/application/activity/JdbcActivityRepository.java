package com.pbw.application.activity;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcActivityRepository implements ActivityRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // tipe training dan race disatuin
    private static final String FIND_ALL_QUERY = "SELECT * FROM Activity WHERE id_runner = ?";

    // hanya untuk tipe training
    private static final String INSERT_QUERY = "INSERT INTO activity (judul, tipe_training, createdAt, durasi, jarak, elevasi, description, id_runner, urlpath) VALUES (?, ?::tipeAct, ?::timestamp, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<Activity> findAll(int id_runner) {
    

        List<Activity> result = jdbcTemplate.query(FIND_ALL_QUERY, new ActivityRowMapper(),id_runner);
        System.out.println("result size: "+result.size());

        return result.size() >0 ? result : null;
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
            activity.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
            activity.setDurasi(rs.getString("durasi"));
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

    @Override
    public List<Activity> findTrainingOnlyByIdRunner(int id_runner) {
        String sql = "Select * FROM activity WHERE id_training IS NOT NULL AND id_runner = ?";

        List<Activity> result = jdbcTemplate.query(sql, new ActivityRowMapper(), id_runner);

        return result.size() >0 ? result : null;

    }

    @Override
    public int getIdTrainingOfRaceParticipant(int id_runner, int id_race) {
        String sql = "Select id_training FROM raceparticipants WHERE id_runner = ? AND id_race = ?";

        System.out.println("id runner & id_race" + id_runner+" "+id_race);

        Integer id_training = jdbcTemplate.queryForObject(sql, this::mapIdTraining,id_runner, id_race );

        return id_training != null ? id_training.intValue() : -1; 
        
    }

    @Override
    public Activity getSubmitedActivityOnRace(int id_training) {
        String sql = "Select * FROM activity WHERE id_training = ?";

        return jdbcTemplate.queryForObject(sql, new ActivityRowMapper(), id_training);
    }


    private int mapIdActivity(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_activity");
    }

    private int mapIdTraining(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_training");
    }

    @Override
    public Activity findById(int id) {
        String sql = "SELECT * FROM activity WHERE id_activity = ?";
        return jdbcTemplate.queryForObject(sql, new ActivityRowMapper(), id);
    }

    

    


    
}