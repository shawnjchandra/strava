package com.pbw.application.race;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class RaceWinner {
    @NotNull
    private int id_runner;
    @NotNull
    private String nama;
    @NotNull
    private float jarak;
    @NotNull
    private String durasi;
}
