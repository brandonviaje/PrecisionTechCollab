package com.precisiontech.moviecatalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    // This method maps to the root URL ("/") and returns the index.html page
    @GetMapping("/")
    public String showHomePage() {
        return "components/index";  // "index" corresponds to the index.html file in templates
    }
}