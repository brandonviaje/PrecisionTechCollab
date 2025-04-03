package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddMovies {

    private final WebClient webClient;
    private final SupabaseConfig supabaseConfig;

    @Autowired
    public AddMovies(WebClient webClient, SupabaseConfig supabaseConfig) {
        this.webClient = webClient;
        this.supabaseConfig = supabaseConfig;
    }

    /**
     * Adds a movie to the database
     *
     * @param movie     The movie object being added
     */
    public void addMovie(Movie movie) {
        Map<String, Object> movieData = getStringObjectMap(movie);
        //serialize to json
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(movieData);

            String response = webClient.post()
                    .uri("/rest/v1/movies")
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Prefer", "return=representation")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            //convert to JSON
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseNode = responseMapper.readTree(response);
            String movieId = responseNode.get(0).get("movie_id").asText();

            System.out.println("Movie added with ID: " + movieId);
            movie.setMovieId(movieId);

        } catch (Exception e) {
            System.err.println("Error adding movie: " + e.getMessage());
        }
    }

    /**
     * Helper function for storing movie metadata in a better format
     *
     * @param movie     The movie object
     * @return a map of the movie metadata
     */
    private static Map<String, Object> getStringObjectMap(Movie movie) {
        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", movie.getTitle());
        movieData.put("release_date", movie.getReleaseDate());
        movieData.put("poster_path", movie.getPosterPath());
        movieData.put("genres", movie.getGenres());
        movieData.put("synopsis", movie.getSynopsis());
        movieData.put("pg_rating", movie.getPgRating());
        movieData.put("runtime", movie.getRuntime());
        movieData.put("production_companies", movie.getProductionCompanies());
        movieData.put("spoken_languages", movie.getSpokenLanguages());
        return movieData;
    }
}