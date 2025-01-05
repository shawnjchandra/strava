package com.pbw.application.config.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final LoginInterceptor loginInterceptor;
    
    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/css/error/**")
                .addResourceLocations("classpath:/static/css/error/");
        registry.addResourceHandler("/css/race/**")
                .addResourceLocations("classpath:/static/css/race/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");

    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
               .addPathPatterns("/**")
               .excludePathPatterns("/static/**", "/css/**","/css/error/**","/css/race/**", "/js/**", "/img/**", "/assets/**", 
                                  "/", "/login", "/register");
    }
}