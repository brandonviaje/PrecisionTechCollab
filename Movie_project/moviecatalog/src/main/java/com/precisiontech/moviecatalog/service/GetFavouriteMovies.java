package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GetFavouriteMovies {

    private final WebClient webClient;
    private final String supabaseApiKey;

    @Autowired
    public GetFavouriteMovies(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
    }


    /**
     * Retrieves a user's favourite movie by the username assigned to the movie
     *
     * @param username      the user's username
     * @return a list of the user's favourite movies
     */
    public List<Movie> getFavouriteMovieByUsername(String username) {
        List<Movie> userFavourites;

        // Fetch movies from Supabase where the title matches the search query
        userFavourites = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/favourites")
                        .queryParam("username", "ilike.%" + username + "%")  // Using ilike for case-insensitive search
                        .queryParam("order", "id.asc")  // Sorting by ID in ascending order
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();

        return userFavourites;
    }
}
