package com.precisiontech.moviecatalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Spring Boot controller method to handle HTTP GET requests to the "/" endpoint
     * Handles loading the home page
     *
     * @return          the home page
     */
    @GetMapping("/")
    public String showHomePage() {
        return "index";  // "index" corresponds to the index.html file in templates
    }
}