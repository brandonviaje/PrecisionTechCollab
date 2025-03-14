package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
public class MovieService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api.key}") // Use Service Role Key (since RLS is disabled)
    private String supabaseApiKey;

    private WebClient webClient;
    private List<Movie> movies = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

    public Movie addMovie(Movie movie) {
        movies.add(movie);

        // Prepare the movie data to send to Supabase
        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", movie.getTitle());
        movieData.put("release_date", movie.getReleaseDate());
        movieData.put("runtime", movie.getRuntime());
        movieData.put("pg_rating", movie.getPgRating());
        movieData.put("synopsis", movie.getSynopsis());
        movieData.put("poster_path", movie.getPosterPath());
        movieData.put("genres", movie.getGenres());
        movieData.put("production_companies", movie.getProductionCompanies());
        movieData.put("production_countries", movie.getProductionCountries());
        movieData.put("spoken_languages", movie.getSpokenLanguages());

        try {
            // Serialize movie data to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(movieData);

            // Sends data to Supabase asynchronously
            String response = webClient.post()
                    .uri("/rest/v1/movies")
                    .header("apikey", supabaseApiKey)
                    .header("Prefer", "return=representation")  // Ensures we get the inserted row back
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)  // The response will be a JSON string
                    .block();  // Blocking here to wait for the response, this could be changed based on async behavior preference

            // Log the response for debugging purposes
            System.out.println("Supabase Response: " + response);

            // Return the movie object (now saved with the poster path)
            return movie;

        } catch (Exception e) {
            System.err.println("Error adding movie to Supabase: " + e.getMessage());
            throw new RuntimeException("Error adding movie: " + e.getMessage());
        }
    }
    public List<Movie> getAllMovies() {return movies;}

    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> allMovies;

        if (genre == null || genre.isEmpty()) {
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("order", "id.asc")
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();
        } else {
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("genres", "ilike.%"+ genre +"%")
                            .queryParam("order", "id.asc")
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();
        }
        return allMovies;
    }

    public List<Movie> searchMoviesByTitle(String title) {
        List<Movie> searchedMovies;

        if (title == null || title.isEmpty()) {
            searchedMovies = new ArrayList<>();
        } else {
            searchedMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("title", "ilike.%" + title + "%")
                            .queryParam("order", "id.asc")
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
