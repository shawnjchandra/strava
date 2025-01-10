package com.pbw.application.race;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pbw.application.activity.Activity;

@Repository
public class JDBCRaceRepo implements RaceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Activity> getActivityByIdRace(int id_race) {
       String sql = "Select * FROM activity WHERE id_race = ?";

        List<Activity> result = jdbcTemplate.query(sql, this::mapIdToActivityRace, id_race);
    
       return result.size()  == 0 ? Optional.empty(): Optional.of(result.get(0));
    }
    
    @Override
    public List<Integer> getFromRaceParticipants(int id_runner){
        String sql = "Select * FROM raceparticipants WHERE id_runner = ?";

        return jdbcTemplate.query(sql, this::mapIdRunnerToRP, id_runner);
    }

    @Override
    public boolean joinRace(int id_runner, int id_race) {
        String sql = "INSERT INTO raceparticipants (id_runner, id_race) VALUES (?,?)";

        return jdbcTemplate.update(sql, id_runner,id_race) > 0 ? true: false;
    }

    @Override
    public boolean addSubmissionToRace(int id_runner, int id_race, int id_training) {
        String sql = "UPDATE raceparticipants SET id_training = ? WHERE id_runner = ? AND id_race = ?";

        return jdbcTemplate.update(sql, id_training, id_runner, id_race)>0 ? true: false;
    }

    @Override
    public boolean addRace(Activity activity, int id_admin) {
        String sql = "INSERT INTO activity (judul, tipe_race, createdAt, durasi, jarak, description, kuota_max, id_admin) VALUES (?, ?::tipeAct, ?::date, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
            activity.getJudul(),
            activity.getTipeRace(),
            activity.getCreatedAt(),
            activity.getDurasi(),
            activity.getJarak(),
            activity.getDescription(),
            activity.getKuota_max(),
            id_admin
        ) > 0 ? true: false;

    }

    @Override
    public List<Activity> getAllRace() {
        String sql = "Select * FROM activity WHERE id_race IS NOT NULL";

        List<Activity> result = jdbcTemplate.query(sql, this::mapIdToActivityRace);

        return result.size() >0 ? result : null;
    }

    

    @Override
    public int getIdRaceByIdActivity(int id_activity) {
        String sql = "Select id_race FROM activity WHERE id_activity = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, id_activity);
        } catch (EmptyResultDataAccessException e) {
            return -1; 
        }
    
    }

    @Override
    public int getIdTrainingByIdActivity(int id_activity) {
        String sql = "Select id_training FROM activity WHERE id_activity = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, id_activity);
        } catch (EmptyResultDataAccessException e) {
            return -1; 
        }
    
    }


    @Override
    public Optional<Activity> getActivityByIdActivity(int id_activity) {
        String sql = "Select * FROM activity WHERE id_activity = ?";

        List<Activity> result = jdbcTemplate.query(sql, this::mapIdToActivityRace, id_activity);
    
       return result.size()  == 0 ? Optional.empty(): Optional.of(result.get(0));
    }

    @Override
    public List<Integer> getAllParticipantsOfSpecificRace(int id_race) {
        String sql = "SELECT id_runner FROM raceparticipants WHERE id_race = ?";

        List<Integer> result = jdbcTemplate.query(sql, this::mapIdRaceToRP, id_race);

        return result.size()>0 ? result : null;

    }

    @Override
    public List<Activity> getAllRaceFiltered(String judul, String sortBy, String sortOrder) {
        StringBuilder sql = new StringBuilder("Select * FROM activity WHERE id_race IS NOT NULL");
        
        
        List<Object> params = new ArrayList<>();
    
        // Handle keyword search
        if (judul != null && !judul.isEmpty()) {
            sql.append(" AND judul ILIKE ?");  // Use ILIKE for case-insensitive
            params.add("%" + judul + "%");  // Add wildcards here, not in SQL
        }

        // Handle sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ").append(sortBy.toLowerCase());
            if ("desc".equalsIgnoreCase(sortOrder)) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }
    
        return jdbcTemplate.query(sql.toString(), this::mapIdToActivityRace, params.toArray());

    }

    private int mapIdRunnerToRP(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_race");
    }

    private int mapIdRaceToRP(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_runner");
    }
    
    private Activity mapIdToActivityRace(ResultSet rSet, int rowNum) throws SQLException{
        Activity act = new Activity();
        act.setIdActivity(rSet.getInt("id_activity"));
        act.setJudul(rSet.getString("judul"));
        act.setCreatedAt(rSet.getTimestamp("createdAt").toLocalDateTime());
        act.setDurasi(rSet.getString("durasi"));
        act.setJarak(rSet.getInt("jarak"));
        act.setDescription(rSet.getString("description"));
        
        act.setKuota_max(rSet.getInt("kuota_max"));
        act.setTipeRace(rSet.getString("tipe_race"));
        
        return act;
    }

    



}
