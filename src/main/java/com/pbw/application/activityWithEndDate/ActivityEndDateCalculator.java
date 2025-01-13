package com.pbw.application.activityWithEndDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ActivityEndDateCalculator {

    public static LocalDateTime calculateEndDateTime(LocalDateTime createdAt, Duration durasi) {

        return createdAt.plus(durasi);
    }
    

  
}