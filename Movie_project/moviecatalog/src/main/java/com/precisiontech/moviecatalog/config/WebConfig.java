package com.precisiontech.moviecatalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the /userimg/** path to the userimg folder in static directory
        registry.addResourceHandler("/userimg/**")
                .addResourceLocations("classpath:/static/userimg/");
    }
}