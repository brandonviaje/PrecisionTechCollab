package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddFavouriteMovie {

    private final WebClient webClient;
    private final SupabaseConfig supabaseConfig;

    @Autowired
    public AddFavouriteMovie(WebClient webClient, SupabaseConfig supabaseConfig) {
        this.webClient = webClient;
        this.supabaseConfig = supabaseConfig;

    }

    public void addFavouriteMovie(String username, Movie movie) {
        System.out.println("Adding favourite movie for username: " + username);
        System.out.println("Movie details: " + movie);

        Map<String, Object> favouriteMovieData = getStringObjectMap(username, movie);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(favouriteMovieData);

            System.out.println("Payload to be sent: " + jsonPayload);


            webClient.post()
                    .uri("/rest/v1/favourites")
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Prefer", "return=representation")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Favourite movie added successfully.");

        } catch (Exception e) {
            System.err.println("Error adding movie: " + e.getMessage());
        }
    }

    private static Map<String, Object> getStringObjectMap(String username, Movie movie) {
        Map<String, Object> favouriteMovieData = new HashMap<>();
        favouriteMovieData.put("username", username);
        favouriteMovieData.put("title", movie.getTitle());
        favouriteMovieData.put("release_date", movie.getReleaseDate());
        favouriteMovieData.put("poster_path", movie.getPosterPath());
        favouriteMovieData.put("genres", movie.getGenres());
        favouriteMovieData.put("synopsis", movie.getSynopsis());
        favouriteMovieData.put("pg_rating", movie.getPgRating());
        favouriteMovieData.put("runtime", movie.getRuntime());
        favouriteMovieData.put("production_companies", movie.getProductionCompanies());
        favouriteMovieData.put("spoken_languages", movie.getSpokenLanguages());
        return favouriteMovieData;
    }
}
