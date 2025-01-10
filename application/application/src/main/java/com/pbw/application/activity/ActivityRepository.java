package com.pbw.application.activity;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository {
    List<Activity> findAll(int id_runner);
    void save(Activity activity);
    int getIdActivity();

    public List<Activity> findTrainingOnlyByIdRunner(int id_runner);
    public int getIdTrainingOfRaceParticipant(int id_runner, int id_race);
    public Activity getSubmitedActivityOnRace(int id_training);

    public List<Activity> findAllFilteredTraining(int id_runner, String keywords, String type, String sortBy, String sortOrder);

}