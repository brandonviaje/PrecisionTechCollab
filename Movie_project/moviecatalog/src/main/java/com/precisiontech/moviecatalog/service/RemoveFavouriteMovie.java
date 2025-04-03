package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RemoveFavouriteMovie {

    private final WebClient webClient;
    private final SupabaseConfig supabaseConfig;

    @Autowired
    public RemoveFavouriteMovie(WebClient webClient, SupabaseConfig supabaseConfig) {
        this.webClient = webClient;
        this.supabaseConfig = supabaseConfig;
    }

    public boolean removeFavouriteMovie(String username, String movieId) {
        try {
            // Delete from Supabase where username and movie match
            String response = webClient.delete()
                    .uri("/rest/v1/favourites")
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Prefer", "return=representation")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("select", "*")
                    .header("username", "eq." + username)
                    .header("id", "eq." + movieId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return true;
        } catch (Exception e) {
            System.err.println("Error removing favorite movie: " + e.getMessage());
            return false;
        }
    }
}