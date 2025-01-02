package com.pbw.application.race;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private int mapIdRunnerToRP(ResultSet rSet, int rowNum) throws SQLException {
        return rSet.getInt("id_race");
    }
    
    private Activity mapIdToActivityRace(ResultSet rSet, int rowNum) throws SQLException{
        Activity act = new Activity();
        act.setIdActivity(rSet.getInt("id_activity"));
        act.setJudul(rSet.getString("judul"));
        act.setCreatedAt(rSet.getDate("createdAt").toLocalDate());
        act.setDurasi(rSet.getTime("durasi").toLocalTime());
        act.setJarak(rSet.getInt("jarak"));
        act.setDescription(rSet.getString("description"));
        
        act.setKuota_max(rSet.getInt("kuota_max"));
        act.setTipeRace(rSet.getString("tipe_race"));
        
        return act;
    }





}
