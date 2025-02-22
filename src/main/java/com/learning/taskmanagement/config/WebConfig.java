package com.learning.taskmanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("Configuring resource handlers");
        
        // API requests should not be handled as static resources
        registry.addResourceHandler("/api/**")
               .addResourceLocations("classpath:/null/");

        // Handle static resources for all other paths
        registry.addResourceHandler("/**")
               .addResourceLocations("classpath:/static/")
               .resourceChain(false);

        logger.info("Resource handlers configured successfully");
    }
}

