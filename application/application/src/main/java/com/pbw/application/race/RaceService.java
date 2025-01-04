package com.pbw.application.race;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbw.application.activity.Activity;
import com.pbw.application.custom.CustomResponse;

import jakarta.validation.constraints.Null;

@Service
public class RaceService {
    
    @Autowired
    private RaceRepository raceRepository;

    public CustomResponse<List<Activity>> getAllAvailableRace(int id_runner){
        List<Integer> allId_race = new ArrayList<>();
        List<Integer> availableRace = new ArrayList<>();
        
        // semua id race yang ada
        List<Activity> races = raceRepository.getAllRace();

        if(races == null || races.isEmpty() ){
            return new CustomResponse<>(false, "No Available Races", null);
        }

        for(Activity race: races){
            int id_activity = race.getIdActivity();
            int id_race = raceRepository.getIdRaceByIdActivity(id_activity);

            allId_race.add(id_race);
        }

        // id_race semua yang udah join
        List<Integer> joinedRaces = raceRepository.getFromRaceParticipants(id_runner);

        // jika di id_race di joined racenya belum ada, pisah ke available
        for(int id_race : allId_race){
            if(!joinedRaces.contains(id_race)){
                availableRace.add(id_race);
            }
        }

        // semua activity (race) yang tersedia
        List<Activity> result = new ArrayList<>();
        for(int id_race : availableRace){
            Activity act = getActivityByIdRace(id_race);
            result.add(act);
        }

        return result.size() > 0 ?
        new CustomResponse<>(true, "Available Races Found", result) :
        new CustomResponse<>(false, "No Available Races", null);
    }

    public CustomResponse<List<Activity>> getAllJoinedRace(int id_runner){
        List<Activity> result = new ArrayList<>();
        
        List<Integer> listOfJoinedRace = getFromRaceParticipants(id_runner);

        if(listOfJoinedRace == null || listOfJoinedRace.isEmpty()){
            return new CustomResponse<>(false , "You haven't joined any race yet", null);
        }

        for(int id_race: listOfJoinedRace){
            System.out.println("id race :"+id_race);
            Activity act = getActivityByIdRace(id_race);
            result.add(act);
        }
        System.out.println(result.size()+" "+result.get(0));

        return result.size()>0 ? 
        new CustomResponse<>(true, "Joined Races Found", result) 
        : new CustomResponse<>(false, "No Joined Race", null);
    }

    public CustomResponse<Null> addRace(Activity activity, int id_admin){
        return raceRepository.addRace(activity, id_admin) == true? 
        new CustomResponse<>(true , "Races has been added succesfully", null) : 
        new CustomResponse<>(false , "Failed to add new race", null);
    }


    public CustomResponse<Null> joinRace(int id_runner, int id_activity) {
        int id_race = getIdRaceByIdActivity(id_activity);

        System.out.println(id_runner+" id runner dan id race dari join "+id_race);

        return raceRepository.joinRace(id_runner, id_race) == true ?
        new CustomResponse<>(true, "You have join the race", null) :
        new CustomResponse<>(false, "Failed to join the race", null) ;
        
    }

    public Activity getActivityByIdActivity(int id_activity) {
        Optional<Activity> result = raceRepository.getActivityByIdActivity(id_activity);
 
        return result.isPresent() == true ? result.get() : null; 
    }

    public List<Integer> getParticipantsOfSpecificRace(int id_activity){
        int id_race = getIdRaceByIdActivity(id_activity);

        List<Integer> participantIds = raceRepository.getAllParticipantsOfSpecificRace(id_race);
        
        return participantIds;
    }

    public int getIdRaceByIdActivity(int id_activity){
        return raceRepository.getIdRaceByIdActivity(id_activity);
    }
    
    private Activity getActivityByIdRace(int id_race) {
       Optional<Activity> result = raceRepository.getActivityByIdRace(id_race);

       return result.isPresent() == true ? result.get() : null; 
    }

    private List<Integer> getFromRaceParticipants(int id_runner){
        return raceRepository.getFromRaceParticipants(id_runner);
    }

    public int getIdTrainingByIdActivity(int id_activity){
        return raceRepository.getIdTrainingByIdActivity(id_activity);
    }

    public boolean addSubmissionToRace(int id_runner, int id_activity_race, int id_activity_training){
        int id_race = getIdRaceByIdActivity(id_activity_race);

        int id_training = getIdTrainingByIdActivity(id_activity_training);
        
        return raceRepository.addSubmissionToRace(id_runner, id_race, id_training);
    } 

}
