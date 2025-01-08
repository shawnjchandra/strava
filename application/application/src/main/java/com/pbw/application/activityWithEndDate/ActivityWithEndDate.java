package com.pbw.application.activityWithEndDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.pbw.application.activity.Activity;
import com.pbw.application.activity.ActivityService;
import com.pbw.application.custom.CustomResponse;
import com.pbw.application.race.RaceService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ActivityWithEndDate{
    

    @Autowired
    private RaceService raceService;
    
    @Autowired
    private ActivityService activityService;

    Activity activity;
    LocalDateTime endDateTime;

    public List<ActivityWithEndDate> getListOfActsWithEndDate(int id_runner){
        CustomResponse<List<Activity>> listOfTraining = activityService.findTrainingOnlyByIdRunner(id_runner);
        List<ActivityWithEndDate> result = new ArrayList<>();

        for(Activity act : listOfTraining.getData()){
            ActivityWithEndDate awend = new ActivityWithEndDate();                
            
            LocalDateTime endDateTime = getEndDate(act);

            awend.setActivity(act);
            awend.setEndDateTime(endDateTime);
            
            result.add(awend);
        }
        // Masih bisa null atau empty;
        return result;

    }

    public ActivityWithEndDate getSingleActWithEndDate(int id_activity){

        Activity race = raceService.getActivityByIdActivity(id_activity);

        ActivityWithEndDate awend = new ActivityWithEndDate(); 
        LocalDateTime endDateTime = getEndDate(race);

        awend.setActivity(race);
        awend.setEndDateTime(endDateTime);

        return awend;

    }

    private LocalDateTime getEndDate(Activity act){


        LocalDateTime createdAt = act.getCreatedAt(); 

        String durasiString = act.getDurasi();
        String[] parts = durasiString.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);

        Duration durasi = Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);

        
        return ActivityEndDateCalculator.calculateEndDateTime(createdAt, durasi);
    }
}