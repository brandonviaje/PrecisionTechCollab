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

    /**
     * Spring Boot controller method to handle HTTP POST requests sent from the front end to the "/movies" endpoint
     * Handles the submission of a movie by an admin
     *
     * @param title             title of the movie
     * @param releaseDate       release date of the movie
     * @param poster            cover image of the movie
     * @param genres            genres of the movie
     * @param synopsis          synopsis of the movie
     * @return                  path to the cover image
     */
    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestParam("title") String title, @RequestParam("releaseDate") String releaseDate, @RequestParam("poster") MultipartFile poster, @RequestParam("genres") String genres, @RequestParam("synopsis") String synopsis) {

        // Save the poster file as a string path, return poster path
        String posterPath = movieService.saveImage(poster);
        Movie movie = new Movie(title, releaseDate, posterPath, genres, synopsis); //create movie obj
        movieService.addMovie(movie);  // save object to the database

        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/movies" endpoint
     * Handles the filtering of movies by the user
     *
     * @param genre         optional genre filter
     * @param pgRating      Optional PG rating filter
     * @param language      Optional language filter.
     * @return              movies gathered from the database
     */
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies (
        @RequestParam(value = "genre", required = false) String genre,
        @RequestParam(value = "pg_rating", required = false) String pgRating,
        @RequestParam(value = "spoken_languages", required = false) String languages){
        
        List<Movie> movies;
        //if genre is found, find movies by that genre 
        if(genre != null){
            movies = movieService.filterByGenre(genre);
        //if genre is not selected, but pgrating is selected, find movies by that rating
        }else if(pgRating != null){
            movies = movieService.filterByPgRating(pgRating);
        //if genre & rating is not selected, but langauges is selected, find movies by that langauge
        }else if(languages != null){
            movies = movieService.filterByLanguage(languages);
        //if nothign is selected, show all movies 
        }else{
            movies = movieService.getAllMovies();
        }

        return ResponseEntity.ok(movies);
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/movies/search" endpoint
     * Handles the user searching for a movie
     *
     * @param title         title of the movie to be searched for
     * @return              movies with the search filter applied
     */
    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies (@RequestParam(value = "title") String title){
        List<Movie> movies = movieService.searchMovies(title);
        return ResponseEntity.ok(movies);
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/movies/{movieId}" endpoint
     *
     * @param movieId           id of the movie
     * @return                  movie with the specified id
     */
    @GetMapping("/movies/{movieId}")
    public Movie getMovieDetails (@PathVariable String movieId){
        return movieService.getMovieById(movieId);
    }

}


