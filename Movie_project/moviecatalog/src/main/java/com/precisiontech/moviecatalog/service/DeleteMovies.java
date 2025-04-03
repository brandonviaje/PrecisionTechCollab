package com.precisiontech.moviecatalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.annotation.PostConstruct;

@Service
public class DeleteMovies {

    /**
     * The URL of the Supabase database, injected from the application properties.
     */
    @Value("${supabase.url}")
    String supabaseUrl;


    /**
     * The API key for authenticating with Supabase, injected from the application properties.
     */
    @Value("${supabase.api.key}")
    String supabaseApiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

    /**
     * Deletes a movie from the database by its movie ID.
     *
     * @param movieId The id of the movie to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteMovie(String movieId) {
        try {
            String response = webClient.delete()
                    .uri("/rest/v1/movies?movie_id=eq." + movieId)
                    .header("apikey", supabaseApiKey)
                    .header("Prefer", "return=minimal")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return response == null || response.isEmpty();
        } catch (Exception e) {
            System.err.println("Error in deleting movie: " + e.getMessage());
            return false;
        }
    }
}
