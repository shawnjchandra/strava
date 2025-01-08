package com.pbw.application.activity;


import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Activity {
    
    // global
    private int idActivity;
    private String judul;
    private LocalDateTime createdAt;
    private String durasi;
    private float jarak;
    private float elevasi;
    private String description;
    private String urlpath;
    
    
    // training
    private Integer idRunner;
    private String tipeTraining;

    // race
    private int kuota_max;
    private String tipeRace;
    
}