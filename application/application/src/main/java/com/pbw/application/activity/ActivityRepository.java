package com.pbw.application.activity;

import java.util.List;

public interface ActivityRepository {
    List<Activity> findAll(int id_runner);
    void save(Activity activity);
    int getIdActivity();

    public List<Activity> findTrainingOnlyByIdRunner(int id_runner);
    public int getIdTrainingOfRaceParticipant(int id_runner, int id_race);
    public Activity getSubmitedActivityOnRace(int id_training);
}