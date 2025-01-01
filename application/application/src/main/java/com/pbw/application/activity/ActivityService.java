package com.pbw.application.activity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pbw.application.custom.CustomResponse;

@Service
public class ActivityService {
    

    @Autowired
    private ActivityRepository activityRepository;

    public int getIdActivity(){
        return activityRepository.getIdActivity();
    }

    public List<Activity> findAll(){
        return activityRepository.findAll();
    }

    public void save(Activity activity){
        activityRepository.save(activity);
    }

}
