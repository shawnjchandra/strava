package com.pbw.application.activity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbw.application.activityWithEndDate.ActivityWithEndDate;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.user.UserService;


@Service
public class ActivityService {
    

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired UserService userService;

    public int getIdActivity(){
        return activityRepository.getIdActivity();
    }

    public List<Activity> findAllByIdUser(int id_user){
        
        // dari id_user harus convert ke id_runner;
        int id_runner = userService.getIdRunnerByIdUsers(id_user);

        //System.out.println("iduser -> idrunner"+id_user+" "+id_runner);

        return activityRepository.findAll(id_runner);
    }

    public void save(Activity activity){
        activityRepository.save(activity);
    }

    public CustomResponse<List<Activity>> findTrainingOnlyByIdRunner(int id_runner){
        List<Activity> result = activityRepository.findTrainingOnlyByIdRunner(id_runner);
    
        return result != null ?
        new CustomResponse<>(true, "Trainings have been found", result) :
        new CustomResponse<>(false, "No Trainings have been found", null);
    }

    public CustomResponse<List<Activity>> findTrainingAccordingToType(int id_runner, String type){
        List<Activity> result = activityRepository.findTrainingAccordingToType(id_runner, type);
    
        return result != null ?
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

    public CustomResponse<List<Activity>> filterTrainingAccordingDate(List<Activity> activities, ActivityWithEndDate awend){
        if(activities == null || activities.isEmpty()){
            return new CustomResponse<>(false, "No Trainings have been found", null);
        }


        List<Activity> result = new ArrayList<>();
        
        LocalDateTime startDateTime = awend.getActivity().getCreatedAt();
        LocalDateTime endDateTime = awend.getEndDateTime();

        for(Activity act: activities){
            LocalDateTime createdAt = act.getCreatedAt();
            //System.out.println("di according date : "+ act);
            if(createdAt.isEqual(startDateTime) || 
                createdAt.isEqual(endDateTime) || 
                (createdAt.isAfter(startDateTime) && createdAt.isBefore(endDateTime))) {

            result.add(act);
            }
        }

        return result.size() >0 ?
        new CustomResponse<>(true, "Trainings have been found", result) :
        new CustomResponse<>(false, "No Trainings have been found", null);
    }

    public List<Activity> findAllFilteredTraining(int id_runner, String keywords, String type,String sortBy, String sortOrder){
        return activityRepository.findAllFilteredTraining(id_runner, keywords, type, sortBy, sortOrder);
    }
    

    public CustomResponse<Boolean> isRaceEnded(ActivityWithEndDate awend){
        boolean isValid = false;
        
        LocalDateTime startDateTime = awend.getActivity().getCreatedAt();
        LocalDateTime endDateTime = awend.getEndDateTime();

        LocalDateTime currentDateTime = LocalDateTime.now();

        if(currentDateTime.isEqual(startDateTime) || 
            currentDateTime.isEqual(endDateTime) || 
            (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime))) {

            isValid = true;

        }
        return isValid ? 
        new CustomResponse<>(isValid, "Race is still ongoing", null) :
        new CustomResponse<>(isValid, "Race has ended", null);

    }


}
