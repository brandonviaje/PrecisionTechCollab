package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Movie;
import com.precisiontech.moviecatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Base path for all endpoints
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/movies")
    public ResponseEntity<?> handleMovieSubmission(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.addMovie(movie);
            return ResponseEntity.ok(savedMovie); // Return inserted movie
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding movie: " + e.getMessage());
        }
    }
}
