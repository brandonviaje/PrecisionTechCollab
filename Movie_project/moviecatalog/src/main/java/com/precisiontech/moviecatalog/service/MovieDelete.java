package com.precisiontech.moviecatalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class MovieDelete {
@Value("${supabase.url}")
    private String supabaseUrl;

    // Use Service Role Key (since RLS is disabled)
    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private WebClient webClient;
    private List<Movie> movies = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

    /**
     * Deletes a movie from the database by its title.
     *
     * @param title The title of the movie to delete.
     * @return true if deletion was successful, false otherwise.
     */
    
    public boolean deleteMovieByName(String title){
        try {
            String response = webClient.delete()
                            .uri(uriBuilder -> uriBuilder
                                .path(supabaseUrl + "/rest/v1/movies")
                                .queryParam("title", "eq." + title)//Find the movie by title
                                .build())
                            .header("apikey", supabaseApiKey)
                            .header("Prefer", "return=minimal") 
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
            
            return response == null || response.isEmpty();
        } catch (Exception e) {
            System.err.println("Error in deleting movie" + e.getMessage());
            return false;
        }
    }
    
}
