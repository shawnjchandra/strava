package com.pbw.application.admin;

import java.time.LocalDate;

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
public class Admin {
    @NotNull
    @Size(min=4,max=30)
    private String email;    //email
    
    @NotNull
    @Size(min=4,max=60)
    private String password;
    
    @NotNull
    @Size(min=4,max=60)
    private String confirmpassword;

}