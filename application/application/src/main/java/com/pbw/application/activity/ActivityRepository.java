package com.pbw.application.activity;

import java.util.List;

public interface ActivityRepository {
    List<Activity> findAll();
    void save(Activity activity);
}