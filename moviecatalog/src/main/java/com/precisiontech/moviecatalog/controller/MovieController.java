package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Movie;
import com.precisiontech.moviecatalog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MovieController {

    private final GetFavouriteMovies favouriteGetService;
    private final AddFavouriteMovie favouriteAddService;
    private final AddMovies movieAddService;
    private final EditMovies movieEditService;
    private final DeleteMovies movieDeleteService;
    private final FilterMovies movieFilterService;
    private final SearchMovies movieSearchService;
    private final FetchMovies movieFetchService;
    private final GetMovieDetails movieDetails;
    private final RemoveFavouriteMovie removeFavouriteMovie;


    @Autowired
    public MovieController(GetFavouriteMovies favouriteGetService, AddFavouriteMovie favouriteAddService, AddMovies movieAddService, EditMovies movieEditService, DeleteMovies movieDeleteService, FilterMovies movieFilterService, SearchMovies movieSearchService, FetchMovies movieFetchService, GetMovieDetails movieDetails, RemoveFavouriteMovie removeFavouriteMovie) {
        this.favouriteGetService = favouriteGetService;
        this.favouriteAddService = favouriteAddService;
        this.movieAddService = movieAddService;
        this.movieEditService = movieEditService;
        this.movieDeleteService = movieDeleteService;
        this.movieFilterService = movieFilterService;
        this.movieSearchService = movieSearchService;
        this.movieFetchService = movieFetchService;
        this.movieDetails = movieDetails;
        this.removeFavouriteMovie = removeFavouriteMovie;
    }

    /**
     * Spring Boot controller method to handle HTTP POST requests sent from the front end to the "/favourites" endpoint
     * Handles the addition of a movie to the user's favourites list
     *
     * @param username      the user's username
     * @param movie         the movie object to be added
     * @return              a message indicating the movie was added
     */
    @PostMapping("/favourites")
    public ResponseEntity<?> addFavouriteMovie(@RequestParam("username") String username, @RequestBody Movie movie) {
        // Log the username and movie data to verify the input
        System.out.println("Username: " + username);
        System.out.println("Movie: " + movie.toString()); // Make sure the Movie class has a meaningful toString() method

        favouriteAddService.addFavouriteMovie(username, movie);
        return ResponseEntity.ok("Favourite movie added successfully");
    }

    /**
     * Spring Boot controller method to handle HTTP DELETE requests sent from the front end to the "/favourites/{movieId}" endpopint
     * Handles the deletion of a movie from the user's favourites list
     *
     * @param movieId       the id of the movie being removed
     * @param username      the user's username
     * @return              a message indicating the movie was removed
     */
    @DeleteMapping("/favourites/{movieId}")
    public ResponseEntity<?> removeFavouriteMovie(
            @PathVariable String movieId,
            @RequestParam("username") String username) {
        boolean removed = removeFavouriteMovie.removeFavouriteMovie(username, movieId);

        if (removed) {
            return ResponseEntity.ok("Movie removed from favorites successfully");
        } else {
            // More informative error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Movie not found in favorites for user: " + username);
        }
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/favourites" endpoint
     * Handles the collection of the user's favourite movies
     *
     * @param username      the user's username
     * @return              a message displaying the user's favourite movies
     */
    @GetMapping("/favourites")
    public ResponseEntity<Object> getFavorites(@RequestParam("username") String username) {
        // Use the favouriteGetService to get the list of favorite movies for the user
        List<Movie> movies = favouriteGetService.getFavouriteMovieByUsername(username);  // Corrected method name

        if (movies == null || movies.isEmpty()) {
            // If no movies are found, return a response with both the message and an empty list
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No favourite movies found for user: " + username);
            response.put("favorites", Collections.emptyList());
            return ResponseEntity.ok(response);
        }

        // Otherwise, return the list of movies
        return ResponseEntity.ok(movies);
    }

    /**
     * Spring Boot controller method to handle HTTP POST requests sent from the front end to the "/movies" endpoint
     * Handles the addition of a movie to the database
     *
     * @param title                     the movie's title
     * @param releaseDate               the movie's release date
     * @param poster                    the movie poster
     * @param genres                    the movie's genres
     * @param synopsis                  the movie's synopsis
     * @param pg_rating                 the movie's pg rating
     * @param production_companies      the production companies who made the movie
     * @param runtime                   the movie's runtime
     * @param spoken_languages          the languages spoken in the movie
     * @return                          a message indicating whether the movie was added to the database
     */
    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestParam("title") String title, @RequestParam("releaseDate") String releaseDate, @RequestParam("poster") MultipartFile poster, @RequestParam("genres") String genres, @RequestParam("synopsis") String synopsis, @RequestParam("pgRating") String pg_rating, @RequestParam("productionCompanies") String production_companies, @RequestParam("runtime") int runtime, @RequestParam("spokenLanguages") String spoken_languages) {
        String posterPath = SaveImage.saveImage(poster);
        Movie movie = new Movie(title, releaseDate, posterPath, genres, synopsis, pg_rating, production_companies, runtime, spoken_languages);
        movieAddService.addMovie(movie);
        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/movies" endpoint
     * Handles the collection of movies and specified details from the general database
     *
     * @param genre             the genre to be filtered by
     * @param pgRating          the pg rating to be filtered by
     * @param languages         the language to be filtered by
     * @return                  the movie fitting all parameters
     */
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(value = "genre", required = false) String genre, @RequestParam(value = "pg_rating", required = false) String pgRating, @RequestParam(value = "spoken_languages", required = false) String languages) {
        List<Movie> movies = movieFilterService.filterMovies(genre,pgRating,languages);
        return ResponseEntity.ok(movies);
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/movies/search" endpoint
     * Handles the collection of movies for a user-search
     *
     * @param title     the movie's title
     * @return          the movie found in the database
     */
    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam(value = "title") String title) {
        List<Movie> movies = movieSearchService.searchMovies(title);
        return ResponseEntity.ok(movies);
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/movies/{movieId}" endpoint
     * Handles the collection of a movie's metadata
     *
     * @param movieId       the id associated with the movie
     * @return              the details of the movie
     */
    @GetMapping("/movies/{movieId}")
    public Movie getMovieDetails(@PathVariable String movieId) {
        return movieDetails.getMovieById(movieId);
    }

    /**
     * Spring Boot controller method to handle HTTP DELETE requests sent from the front end to the "/movies/delete/{movieId}" endpoint
     * Handles the deletion of a movie from the general database
     *
     * @param movieId       the id associated with the movie
     * @return              a message indicating whether the movie was deleted
     */
    @DeleteMapping("/movies/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable String movieId) {
        boolean deleted = movieDeleteService.deleteMovie(movieId);
        if (deleted) {
            return ResponseEntity.ok("The movie has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The movie was not found");
        }
    }

    /**
     * Spring Boot controller method to handle HTTP PATCH requests sent from the front end to the "/movies/{movieId}/update" endpoint
     * Handles the modification of a movie from the general database
     *
     * @param movieId                   the id associated with the movie
     * @param title                     the title of the movie
     * @param releaseDate               the movie's release date
     * @param genres                    the movie's genres
     * @param synopsis                  the movie's synopsis
     * @param pgRating                  the movie's pg rating
     * @param productionCompanies       the production companies that made the movie
     * @param runtime                   the movie's runtime
     * @param spokenLanguages           the languages spoken in the movie
     * @param poster                    the movie's cover image
     * @return                          a message indicating the movie was successfully updated
     */
    @PatchMapping("/movies/{movieId}/update")
    public ResponseEntity<String> updateMovie(@PathVariable String movieId, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "releaseDate", required = false) String releaseDate, @RequestParam(value = "genres", required = false) String genres, @RequestParam(value = "synopsis", required = false) String synopsis, @RequestParam(value = "pgRating", required = false) String pgRating, @RequestParam(value = "productionCompanies", required = false) String productionCompanies, @RequestParam(value = "runtime", required = false) Integer runtime, @RequestParam(value = "spokenLanguages", required = false) String spokenLanguages, @RequestParam(value = "poster", required = false) MultipartFile poster) {

        Movie existingMovie = movieDetails.getMovieById(movieId);
        if (existingMovie == null) {return ResponseEntity.notFound().build();}

        // Update fields if they are provided
        if (title != null) existingMovie.setTitle(title);
        if (releaseDate != null) existingMovie.setReleaseDate(releaseDate);
        if (genres != null) existingMovie.setGenres(genres);
        if (synopsis != null) existingMovie.setSynopsis(synopsis);
        if (pgRating != null) existingMovie.setPgRating(pgRating);
        if (productionCompanies != null) existingMovie.setProductionCompanies(productionCompanies);
        if (runtime != null) existingMovie.setRuntime(runtime);
        if (spokenLanguages != null) existingMovie.setSpokenLanguages(spokenLanguages);

        if (poster != null && !poster.isEmpty()) {
            String posterPath = SaveImage.saveImage(poster);
            existingMovie.setPosterPath(posterPath);
        }
        movieEditService.updateMovieDetails(movieId, existingMovie);
        return ResponseEntity.ok("Movie updated successfully!");
    }
}
