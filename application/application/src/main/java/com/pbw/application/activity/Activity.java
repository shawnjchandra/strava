package com.pbw.application.activity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Activity {
    private int idActivity;
    private String judul;
    private String tipeTraining;
    private LocalDate createdAt;
    private LocalTime durasi;
    private int jarak;
    private int elevasi;
    private String description;
    private Integer idRunner;

    // Getters and Setters
    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idActivity) {
        this.idActivity = idActivity;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTipeTraining() {
        return tipeTraining;
    }

    public void setTipeTraining(String tipeTraining) {
        this.tipeTraining = tipeTraining;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalTime getDurasi() {
        return durasi;
    }

    public void setDurasi(LocalTime durasi) {
        this.durasi = durasi;
    }

    public int getJarak() {
        return jarak;
    }

    public void setJarak(int jarak) {
        this.jarak = jarak;
    }

    public int getElevasi() {
        return elevasi;
    }

    public void setElevasi(int elevasi) {
        this.elevasi = elevasi;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdRunner() {
        return idRunner;
    }

    public void setIdRunner(Integer idRunner) {
        this.idRunner = idRunner;
    }
}
