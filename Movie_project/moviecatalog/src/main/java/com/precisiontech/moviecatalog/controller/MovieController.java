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
    public ResponseEntity<?> addMovie(@RequestParam("title") String title, @RequestParam("releaseDate") String releaseDate, @RequestParam("poster") MultipartFile poster) {

        // Save the poster file and get the file path
        String posterPath = saveImage(poster);

        // Create the movie object with the poster path
        Movie movie = new Movie(title, releaseDate, posterPath);

        // Save the movie object to the database
        movieService.addMovie(movie);

        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }

    // Save the image to the userimg folder and return the relative path
    private String saveImage(MultipartFile poster) {
        try {
            // Generate a unique filename for the image
            String imageName = poster.getOriginalFilename();
            Path path = Paths.get("src", "main", "resources", "static", "userimg", imageName); // Store the image inside the userimg folder

            // Create the directory if it doesn't exist
            Files.createDirectories(path.getParent());

            // Save the file to the specified path
            poster.transferTo(path);

            // Return the relative path to the image (use forward slashes for URL compatibility)
            return "/userimg/" + imageName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage());
        }
    }

    // Get all movies with optional genre filter
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(value = "genre", required = false) String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    // Search for movies by title
    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam(value = "title") String title) {
        List<Movie> movies = movieService.searchMoviesByTitle(title);
        return ResponseEntity.ok(movies);
    }
}

