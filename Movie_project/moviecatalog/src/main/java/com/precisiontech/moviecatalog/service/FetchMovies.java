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
public class FetchMovies {

    private final WebClient webClient;
    private final String supabaseApiKey;

    @Autowired
    public FetchMovies(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
    }

    /**
     * Fetches all movies from the database
     *
     * @return List of all movies
     */
    public List<Movie> getAllMovies() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/movies")
                        .queryParam("order", "id.asc")  // Sorting by ID in ascending order
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }
}
