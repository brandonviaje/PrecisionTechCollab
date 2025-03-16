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
import java.util.UUID;

@RestController
@RequestMapping("/api") // Base path for all endpoints
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestParam("title") String title, @RequestParam("releaseDate") String releaseDate, @RequestParam("poster") MultipartFile poster, @RequestParam("genres") String genres, @RequestParam("synopsis") String synopsis) {

        // Save the poster file as a string path, return poster path
        String posterPath = movieService.saveImage(poster);
        Movie movie = new Movie(title, releaseDate, posterPath, genres, synopsis); //create movie obj
        movieService.addMovie(movie);  // save object to the database

        //Response with status code 200 OK
        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }
        public ResponseEntity<?> addMovie (@RequestParam("title") String title, @RequestParam("releaseDate") String
        releaseDate, @RequestParam("poster") MultipartFile poster, @RequestParam("synopsis") String synopsis){

            // Save the poster file and get the file path
            String posterPath = movieService.saveImage(poster);
            String movieId = UUID.randomUUID().toString();

            // create movie obj with the poster path and generated movieId
            Movie movie = new Movie(movieId, title, releaseDate, posterPath,synopsis);

            // Save the movie object to the database
            movieService.addMovie(movie);

            return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
        }

        // Save the image to the userimg folder and return  relative path
        private String saveImage (MultipartFile poster){
            try {
                // Generate a unique filename for the image
                String imageName = poster.getOriginalFilename();
                Path path = Paths.get("src", "main", "resources", "static", "userimg", imageName); // Store the image inside the userimg folder

                // Create the directory if it doesn't exist
                Files.createDirectories(path.getParent());
                poster.transferTo(path); // Save the file to the specified path

                // return relative path to the image
                return "/userimg/" + imageName;
            } catch (IOException e) {
                throw new RuntimeException("Error saving image: " + e.getMessage());
            }

        }

        // Get all movies with optional genre filter
        @GetMapping("/movies")
        public ResponseEntity<List<Movie>> getMovies (@RequestParam(value = "genre", required = false) String genre){
            List<Movie> movies = movieService.filterByGenre(genre);
            return ResponseEntity.ok(movies);
        }

        // Search for movies by title
        @GetMapping("/movies/search")
        public ResponseEntity<List<Movie>> searchMovies (@RequestParam(value = "title") String title){
            List<Movie> movies = movieService.searchMovies(title);
            return ResponseEntity.ok(movies);
        }


        @GetMapping("/movies/{movieId}")
        public Movie getMovieDetails (@PathVariable String movieId){
            return movieService.getMovieById(movieId);
        }

}


