package com.pbw.application.race;

import java.util.List;
import java.util.Optional;

import com.pbw.application.activity.Activity;
import com.pbw.application.user.User;

public interface RaceRepository {

    public Optional<Activity> getActivityByIdRace(int id_race);
    public Optional<Activity> getActivityByIdActivity(int id_activity);
    public List<Integer> getAllParticipantsOfSpecificRace(int id_race);
    public List<Activity> getAllRace();
    public List<Activity> getAllRaceFiltered(String nama, String sortBy, String sortOrder);
    
    public boolean joinRace(int id_runner, int id_race);
    public boolean addRace(Activity activity, int id_admin);
    public boolean addSubmissionToRace(int id_runner, int id_race, int id_training);
    
    public int getIdRaceByIdActivity(int id_activity);
    public int getIdTrainingByIdActivity(int id_activity);
    
    // Lupa buat dispesifikin
    public List<Integer> getFromRaceParticipants(int id_runner);

    public RaceWinner getWinnerFromRace(int id_race);


}
