package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FetchMovies {

    private final MovieFetcher movieFetcher;

    @Autowired
    public FetchMovies(MovieFetcher movieFetcher) {
        this.movieFetcher = movieFetcher;
    }

    /**
     * Fetch all movies from the database
     *
     * @return List of all movies
     */
    public List<Movie> getAllMovies() {
        return movieFetcher.fetchMovies();
    }
}