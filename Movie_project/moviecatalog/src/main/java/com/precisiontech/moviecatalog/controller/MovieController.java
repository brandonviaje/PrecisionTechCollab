package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Movie;
import com.precisiontech.moviecatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> addMovie(@RequestParam("title") String title,
                                      @RequestParam("releaseDate") String releaseDate,
                                      @RequestParam("poster") MultipartFile poster,
                                      @RequestParam("genres") String genres,
                                      @RequestParam("synopsis") String synopsis,
                                      @RequestParam("pgRating") String pg_rating,
                                      @RequestParam("productionCompanies") String production_companies,
                                      @RequestParam("runtime") int runtime,
                                      @RequestParam("spokenLanguages") String spoken_languages) {
        // Save the poster file as a string path, return poster path
        String posterPath = movieService.saveImage(poster);

        // Create movie object with all fields, including the new ones
        Movie movie = new Movie(title, releaseDate, posterPath, genres, synopsis, pg_rating, production_companies, runtime, spoken_languages);

        // Save object to the database
        movieService.addMovie(movie);

        // Response with status code 200 OK
        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }

    // Get all movies with optional genre filter
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(value = "genre", required = false) String genre) {
        List<Movie> movies = movieService.filterByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    // Search for movies
    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam(value = "title") String title) {
        List<Movie> movies = movieService.searchMovies(title);
        return ResponseEntity.ok(movies);
    }

    //Get Movie Details (gets by movieId)
    @GetMapping("/movies/{movieId}")
    public Movie getMovieDetails(@PathVariable String movieId) {
        return movieService.getMovieById(movieId);
    }

    @PatchMapping("/movies/{movieId}/update")
    public ResponseEntity<String> updateMovie(
            @PathVariable String movieId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "releaseDate", required = false) String releaseDate,
            @RequestParam(value = "genres", required = false) String genres,
            @RequestParam(value = "synopsis", required = false) String synopsis,
            @RequestParam(value = "pgRating", required = false) String pgRating,
            @RequestParam(value = "productionCompanies", required = false) String productionCompanies,
            @RequestParam(value = "runtime", required = false) Integer runtime,
            @RequestParam(value = "spokenLanguages", required = false) String spokenLanguages,
            @RequestParam(value = "poster", required = false) MultipartFile poster) {

        // Handle the movie update logic here
        Movie existingMovie = movieService.getMovieById(movieId);
        if (existingMovie == null) {
            return ResponseEntity.notFound().build();  // Movie not found
        }

        // Update the movie fields if they are not null
        if (title != null) existingMovie.setTitle(title);
        if (releaseDate != null) existingMovie.setReleaseDate(releaseDate);
        if (genres != null) existingMovie.setGenres(genres);
        if (synopsis != null) existingMovie.setSynopsis(synopsis);
        if (pgRating != null) existingMovie.setPgRating(pgRating);
        if (productionCompanies != null) existingMovie.setProductionCompanies(productionCompanies);
        if (runtime != null) existingMovie.setRuntime(runtime);
        if (spokenLanguages != null) existingMovie.setSpokenLanguages(spokenLanguages);

        // Handle the poster if uploaded
        if (poster != null && !poster.isEmpty()) {
            String posterPath = movieService.saveImage(poster);
            existingMovie.setPosterPath(posterPath);
        }

        // Use the service to update the movie in the database
        movieService.updateMovieDetails(movieId, existingMovie);
        return ResponseEntity.ok("Movie updated successfully!");
    }
}
