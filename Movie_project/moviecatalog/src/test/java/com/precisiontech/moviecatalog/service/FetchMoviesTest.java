package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchMoviesTest {

    @Mock
    private MovieFetcher movieFetcher;

    @InjectMocks
    private FetchMovies fetchMovies;

    @Test
    public void testGetAllMovies() {
        // Setup Mock for movieFetcher
        List<Movie> mockMovies = Arrays.asList(
                new Movie("Movie 1", "2022-01-01", "/path/to/poster1", "Action", "An exciting movie", "PG-13", "Universal", 120, "English"),
                new Movie("Movie 2", "2023-02-02", "/path/to/poster2", "Comedy", "A funny movie", "R", "Warner Bros", 110, "English, Spanish")
        );
        when(movieFetcher.fetchMovies()).thenReturn(mockMovies);
        List<Movie> result = fetchMovies.getAllMovies();
        assertEquals(2, result.size());
        System.out.println("Test passed: Successfully fetched " + result.size() + " movies.");
    }
}
