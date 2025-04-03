package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class SearchMovies {

    private final WebClient webClient;
    private final FetchMovies movieFetchService;
    private final SupabaseConfig supabaseConfig;

    @Autowired
    public SearchMovies(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig, FetchMovies movieFetchService) {
        this.supabaseConfig = supabaseConfig;
        String supabaseUrl = supabaseConfig.getSupabaseUrl();
        this.webClient = webClientBuilder.baseUrl(supabaseUrl).build();
        this.movieFetchService = movieFetchService;
    }


    /**
     * Fetches movies from the database filtered by the title
     *
     * @param title         title of the movie
     * @return              movie(s) with the specified title
     */
    public List<Movie> searchMovies(String title) {
        List<Movie> searchedMovies;

        // Get the Supabase API key directly from the config
        String supabaseApiKey = supabaseConfig.getSupabaseApiKey();

        // If no title is provided, return all movies using MovieFetchService
        if (title == null || title.trim().isEmpty()) {
            return movieFetchService.getAllMovies();
        } else {
            // Fetch movies from Supabase where the title matches the search query
            searchedMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("title", "ilike.%" + title + "%")  // Using ilike for case-insensitive search
                            .queryParam("order", "id.asc")  // Sorting by ID in ascending order
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();
        }

        return searchedMovies;
    }
}
