package com.precisiontech.moviecatalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    /**
     * Configures resource handlers to serve static files.
     *
     * @param registry the {@link ResourceHandlerRegistry} to configure resource mappings
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Register the userimg folder to be served from the static directory
        registry.addResourceHandler("/userimg/**")
                .addResourceLocations("file:src/main/resources/static/userimg/"); // The path to the 'userimg' folder
    }
}