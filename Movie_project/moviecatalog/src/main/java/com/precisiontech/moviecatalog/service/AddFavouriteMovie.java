package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
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

    public boolean addFavouriteMovie(String username, Movie movie) {
        try {
            Boolean isAlreadyFavorited = checkIfMovieAlreadyFavorited(username, movie.getMovieId());

            if (Boolean.TRUE.equals(isAlreadyFavorited)) {
                System.out.println("Movie already in favorites. Skipping addition.");
                return false;
            }

            Map<String, Object> favouriteMovieData = getStringObjectMap(username, movie);

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
                    .doOnTerminate(() -> System.out.println("Completed adding favourite"))
                    .block();


            System.out.println("Favourite movie added successfully.");
            return true;

        } catch (Exception e) {
            System.err.println("Error adding movie: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace for debugging
            return false;
        }
    }


    private Boolean checkIfMovieAlreadyFavorited(String username, String movieId) {
        try {
            // Perform a query to check if the movie already exists in favorites for this user
            List<Map<String, Object>> existingFavorites = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/favourites")
                            .queryParam("username", "eq." + username) // Ensure "username" is the correct column in the database
                            .queryParam("movie_id", "eq." + movieId) // Ensure "movie_id" is the correct column in the database
                            .build())
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();

            return existingFavorites != null && !existingFavorites.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking existing favorites: " + e.getMessage());
            return false;
        }
    }


    private static Map<String, Object> getStringObjectMap(String username, Movie movie) {
        Map<String, Object> favouriteMovieData = new HashMap<>();
        favouriteMovieData.put("username", username);

        // Use the external movie ID from the Movie object
        String externalMovieId = movie.getMovieId() != null ? movie.getMovieId() : movie.getMovieId();
        favouriteMovieData.put("movie_id", externalMovieId);

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
