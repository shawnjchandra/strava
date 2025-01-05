package com.pbw.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Get the absolute path to the upload directory
        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        
        // Register the resource handler
        registry.addResourceHandler("/uploads/**")
               .addResourceLocations("file:" + uploadPath)
               .setCachePeriod(0);
    }
}