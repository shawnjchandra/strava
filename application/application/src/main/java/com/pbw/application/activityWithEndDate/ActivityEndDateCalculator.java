package com.pbw.application.activityWithEndDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ActivityEndDateCalculator {
    /**
     * Calculates the end date and time of an activity based on its creation datetime and duration
     * 
     * @param createdAt The exact date and time when the activity was created
     * @param durasi The duration of the activity in TIME format (HH:mm:ss)
     * @return LocalDateTime representing when the activity ends
     */
    public static LocalDateTime calculateEndDateTime(LocalDateTime createdAt, Duration durasi) {
        // Add the duration directly to the creation datetime
        return createdAt.plus(durasi);
    }
    

  
}