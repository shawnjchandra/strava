package com.pbw.application.activity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Activity {
    private int idActivity;
    private String judul;
    private String tipeTraining;
    private LocalDate createdAt;
    private LocalTime durasi;
    private float jarak;
    private float elevasi;
    private String description;
    private Integer idRunner;
    private String urlpath;

}
