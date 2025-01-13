package com.pbw.application.user;

import java.time.LocalDate;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class User {
    @NotNull
    @Size(min=4,max=30)
    private String email;    //email
    
    @NotNull
    @Size(min=4,max=60)
    private String password;
    
    @NotNull
    @Size(min=4,max=60)
    private String confirmpassword;
    
    @NotNull
    @Size(min=4,max=50)
    private String nama;
    
    @NotNull
    private LocalDate tanggal_lahir;
    
    @NotNull
    private String lokasi;
    
    @NotNull
    private String gender;

}
