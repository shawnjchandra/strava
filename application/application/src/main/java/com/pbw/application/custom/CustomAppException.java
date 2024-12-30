package com.pbw.application.custom;

import org.springframework.http.HttpStatus;

public class CustomAppException extends RuntimeException{
    private final HttpStatus status;

    public CustomAppException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }

    public HttpStatus geStatus(){
        return this.status;
    }
    
}
