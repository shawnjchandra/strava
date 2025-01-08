package com.pbw.application.aspect;

import java.io.IOException;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.pbw.application.RequiredRole;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {

    private final HttpSession httpSession;
    private final HttpServletResponse httpServletResponse;

    public AuthorizationAspect(HttpSession httpSession, HttpServletResponse httpServletResponse) {
        this.httpSession = httpSession;
        this.httpServletResponse = httpServletResponse;
    }

    @After("@annotation(requiredRole)")
    public void checkAuthorization(JoinPoint joinPoint
    , RequiredRole requiredRole
    ) throws Throwable{


        // String currentUsername = (String)httpSession.getAttribute("username");
        
        // if(currentUsername == null){
        //     throw new Throwable("User is not logged in");
        // }

        String currentRole = (String)httpSession.getAttribute("role");

        System.out.println("current role: "+currentRole);

        boolean isAllowed = false;
        for(String role : requiredRole.value()){
            if(role.equals("*") || currentRole.equals(role)){
                isAllowed = true;
            }
        }
        
        System.out.println("check: "+ isAllowed);
        if(!isAllowed){
            redirectToUnauthorized();
            // throw new Throwable("Insufficient Permission");
        }
    }

    @Before("execution(* com.example.m08.LoginController.*(..))")
    public void log(){
        // System.out.println("EXECUTING METHOD IN LOGIN CONTROLLER ");
    }

    private void redirectToUnauthorized() throws IOException {
        System.out.println("Redirecting to unauthorized");
        httpServletResponse.sendRedirect("/unauthorized");
    }

}