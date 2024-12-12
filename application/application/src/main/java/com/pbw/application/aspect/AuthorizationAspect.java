package com.pbw.application.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.pbw.application.RequiredRole;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {

    private final HttpSession httpSession;

    
    public AuthorizationAspect(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Around("@annotation(requiredRole)")
    public void checkAuthorization(JoinPoint joinPoint
    , RequiredRole requiredRole
    ) throws Throwable{


        String currentUsername = (String)httpSession.getAttribute("username");
        
        if(currentUsername == null){
            throw new Throwable("User is not logged in");
        }

        String currentRole = (String)httpSession.getAttribute("role");


        boolean isAllowed = false;
        for(String role : requiredRole.value()){
            if(role.equals("*") || currentRole.equals(role)){
                isAllowed = true;
            }
        }

        if(isAllowed == false){
            throw new Throwable("Insufficient Permission");
        }
    }

    @Before("execution(* com.example.m08.LoginController.*(..))")
    public void log(){
        // System.out.println("EXECUTING METHOD IN LOGIN CONTROLLER ");
    }

}
