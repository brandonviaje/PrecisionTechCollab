package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Movie;
import com.precisiontech.moviecatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api") // Base path for all endpoints
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestParam("title") String title, @RequestParam("releaseDate") String releaseDate, @RequestParam("poster") MultipartFile poster,@RequestParam("genres") String genres) {

        // Save the poster file as a string path, return poster path
        String posterPath = movieService.saveImage(poster);
        Movie movie = new Movie(title, releaseDate, posterPath,genres); //create movie obj
        movieService.addMovie(movie);  // save object to the database

        //Response with status code 200 OK
        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }

    // Get all movies with optional genre filter
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(value = "genre", required = false) String genre) {
        List<Movie> movies = movieService.filterByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    // Search for movies by title
    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam(value = "title") String title) {
        List<Movie> movies = movieService.searchMovies(title);
        return ResponseEntity.ok(movies);
    }
}