package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Movie;
import com.precisiontech.moviecatalog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {

    private final AddMovies movieAddService;
    private final EditMovies movieEditService;
    private final DeleteMovies movieDeleteService;
    private final FilterMovies movieFilterService;
    private final SearchMovies movieSearchService;
    private final FetchMovies movieFetchService;
    private final GetMovieDetails movieDetails;

    @Autowired
    public MovieController(AddMovies movieAddService, EditMovies movieEditService, DeleteMovies movieDeleteService, FilterMovies movieFilterService, SearchMovies movieSearchService, FetchMovies movieFetchService, GetMovieDetails movieDetails) {
        this.movieAddService = movieAddService;
        this.movieEditService = movieEditService;
        this.movieDeleteService = movieDeleteService;
        this.movieFilterService = movieFilterService;
        this.movieSearchService = movieSearchService;
        this.movieFetchService = movieFetchService;
        this.movieDetails = movieDetails;
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestParam("title") String title, @RequestParam("releaseDate") String releaseDate, @RequestParam("poster") MultipartFile poster, @RequestParam("genres") String genres, @RequestParam("synopsis") String synopsis, @RequestParam("pgRating") String pg_rating, @RequestParam("productionCompanies") String production_companies, @RequestParam("runtime") int runtime, @RequestParam("spokenLanguages") String spoken_languages) {
        String posterPath = SaveImage.saveImage(poster);
        Movie movie = new Movie(title, releaseDate, posterPath, genres, synopsis, pg_rating, production_companies, runtime, spoken_languages);
        movieAddService.addMovie(movie);
        return ResponseEntity.ok("Movie added successfully with poster at " + posterPath);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(value = "genre", required = false) String genre, @RequestParam(value = "pg_rating", required = false) String pgRating, @RequestParam(value = "spoken_languages", required = false) String languages) {
        List<Movie> movies;
        if (genre != null) {
            movies = movieFilterService.filterByGenre(genre);
        } else if (pgRating != null) {
            movies = movieFilterService.filterByPgRating(pgRating);
        } else if (languages != null) {
            movies = movieFilterService.filterByLanguage(languages);
        } else {
            movies = movieFetchService.getAllMovies();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam(value = "title") String title) {
        List<Movie> movies = movieSearchService.searchMovies(title);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/{movieId}")
    public Movie getMovieDetails(@PathVariable String movieId) {
        return movieDetails.getMovieById(movieId);
    }

    @DeleteMapping("/movies/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable String movieId) {
        boolean deleted = movieDeleteService.deleteMovie(movieId);
        if (deleted) {
            return ResponseEntity.ok("The movie has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The movie was not found");
        }
    }

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
