package com.pbw.application.user;

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
    private String username;
    
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
    private String tanggal_lahir;
    
    @NotNull
    private String lokasi;
    
    @NotNull
    private String gender;

}
