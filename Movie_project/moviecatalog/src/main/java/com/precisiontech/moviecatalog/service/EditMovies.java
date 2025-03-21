package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class EditMovies {
    @Value("${supabase.url}")
    private String supabaseUrl;

    // Use Service Role Key (since RLS is disabled)
    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

     public void updateMovieDetails(String movieId, Movie updatedMovie) {
         Map<String, Object> updateFields = getStringObjectMap(updatedMovie);

         try {
             ObjectMapper objectMapper = new ObjectMapper();
             String jsonPayload = objectMapper.writeValueAsString(updateFields);

             // Send PATCH request to update movie details in the database
             webClient.patch()
                     .uri(uriBuilder -> uriBuilder
                             .path("/rest/v1/movies")
                             .queryParam("movie_id", "eq." + movieId)
                             .build())
                     .header("apikey", supabaseApiKey)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .bodyValue(jsonPayload)
                     .retrieve()
                     .bodyToMono(Void.class)
                     .block();
         } catch (Exception e) {
             throw new RuntimeException("Error updating movie: " + e.getMessage(), e);
         }
     }

    private static Map<String, Object> getStringObjectMap(Movie updatedMovie) {
        Map<String, Object> updateFields = new HashMap<>();

        // Add fields to update, checking for null values
        if (updatedMovie.getTitle() != null) {updateFields.put("title", updatedMovie.getTitle());}
        if (updatedMovie.getReleaseDate() != null) {updateFields.put("release_date", updatedMovie.getReleaseDate());}
        if (updatedMovie.getGenres() != null) {updateFields.put("genres", updatedMovie.getGenres());}
        if (updatedMovie.getSynopsis() != null) {updateFields.put("synopsis", updatedMovie.getSynopsis());}
        if (updatedMovie.getRuntime() != 0) {  updateFields.put("runtime", updatedMovie.getRuntime());}
        if (updatedMovie.getSpokenLanguages() != null) {updateFields.put("spoken_languages", updatedMovie.getSpokenLanguages());}
        if (updatedMovie.getProductionCompanies() != null) {updateFields.put("production_companies", updatedMovie.getProductionCompanies());}
        if (updatedMovie.getPgRating() != null) {updateFields.put("pg_rating", updatedMovie.getPgRating());}
        if (updatedMovie.getPosterPath() != null) {updateFields.put("poster_path", updatedMovie.getPosterPath());}
        return updateFields;
    }
}
