package com.pbw.application.race;

import java.util.List;
import java.util.Optional;

import com.pbw.application.activity.Activity;

public interface RaceRepository {

    public Optional<Activity> getActivityByIdRace(int id_race);
    public Optional<Activity> getActivityByIdActivity(int id_activity);
    public List<Integer> getFromRaceParticipants(int id_runner);
    public boolean joinRace(int id_runner, int id_race);
    public boolean addRace(Activity activity, int id_admin);
    
    public List<Activity> getAllRace();
    public int getIdRaceByIdActivity(int id_activity);
    
    
}
