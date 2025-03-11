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

        // Create a JSON object properly
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
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(movieData);

            webClient.post()
                    .uri("/rest/v1/movies")
                    .header("apikey", supabaseApiKey)
                    .header("Prefer", "return=representation")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> System.out.println("Supabase Response: " + response))
                    .doOnError(error -> System.err.println("Supabase Error: " + error.getMessage()))
                    .subscribe();
        } catch (Exception e) {
            System.err.println("JSON Serialization Error: " + e.getMessage());
        }

        return movie;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> allMovies;

        if (genre == null || genre.isEmpty()) {
            // Fetch all movies when no genre is specified
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
            // Filter movies by genre when genre is specified
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("genres", "ilike." + genre) // Filter by genre
                            .queryParam("order", "id.asc")
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();
        }

        // Limit the result to the first 50 movies
        List<Movie> filteredMovies = new ArrayList<>();
        for (int i = 0; i < Math.min(50, allMovies.size()); i++) {
            filteredMovies.add(allMovies.get(i));
        }

        return filteredMovies;
    }
}
