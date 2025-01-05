package com.pbw.application.activity;

import java.util.List;

public interface ActivityRepository {
    List<Activity> findAll();
    List<Activity> findAllByUserId(int idRunner);
    void save(Activity activity);
    int getIdActivity();
}