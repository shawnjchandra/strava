package com.pbw.application.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomResponse<T> {


    private boolean success;
    private HttpStatus statusCode;
    private String message;
    private T data;

    public CustomResponse(
        HttpStatus statusCode,
        boolean success, 
        String message, 
        T data) {

        this.statusCode =statusCode;
        this.success = success;
        this.message = message;
        this.data = data;
        
    }

    public CustomResponse(
        boolean success,
        String message,
        T data
    ){
        this.success = success;
        this.message = message;
        this.data  = data;

    }



    
    
} 
