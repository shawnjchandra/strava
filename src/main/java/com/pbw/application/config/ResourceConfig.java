package com.pbw.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
 
        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        

        registry.addResourceHandler("/uploads/**")
               .addResourceLocations("file:" + uploadPath)
               .setCachePeriod(0);
    }
}