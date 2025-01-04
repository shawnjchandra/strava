package com.pbw.application.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbw.application.custom.CustomResponse;


@Service
public class ActivityService {
    

    @Autowired
    private ActivityRepository activityRepository;

    public int getIdActivity(){
        return activityRepository.getIdActivity();
    }

    public List<Activity> findAll(int id_runner){
        return activityRepository.findAll(id_runner);
    }

    public void save(Activity activity){
        activityRepository.save(activity);
    }

    public CustomResponse<List<Activity>> findTrainingOnlyByIdRunner(int id_runner){
        List<Activity> result = activityRepository.findTrainingOnlyByIdRunner(id_runner);
    
        return result.size() >0 ?
        new CustomResponse<>(true, "Trainings have been found", result) :
        new CustomResponse<>(false, "No Trainings have been found", null);
    }

    public CustomResponse<Activity> getSubmitedActivityOnRace(int id_runner, int id_race){
        int id_training = activityRepository.getIdTrainingOfRaceParticipant(id_runner, id_race);


        if(id_training>0){


            Activity activity = activityRepository.getSubmitedActivityOnRace(id_training);


            return new CustomResponse<Activity>(true, "Found Submited Training", activity);
        }else{
            return new CustomResponse<Activity>(false, "No Submission Found", null);
        }
    }

}
