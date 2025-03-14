package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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

        // Create JSON payload
        String jsonPayload = String.format("""
                        {
                            "title": "%s",
                            "release_date": "%s",
                            "pg_rating": "%s",
                            "synopsis": "%s",
                            "genres": "%s",
                            "production_companies": "%s",
                            "runtime": %d,
                            "production_countries": "%s",
                            "spoken_languages": "%s"
                        }
                        """,
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPgRating(),
                movie.getSynopsis(),
                movie.getGenres(),
                movie.getProductionCompanies(),
                movie.getRuntime(),
                movie.getProductionCountries(),
                movie.getSpokenLanguages()
        );

        webClient.post()
                .uri("/rest/v1/movies")
                .header("apikey", supabaseApiKey) // Service Role Key for unrestricted access
                .header("Prefer", "return=representation")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(jsonPayload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("Supabase Response: " + response))
                .doOnError(error -> System.err.println("Supabase Error: " + error.getMessage()))
                .subscribe();

        return movie;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> allMovies;

        // If no genre is specified, return all movies ordered by the automatic ID (ascending)
        if (genre == null || genre.isEmpty()) {
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("order", "id.asc") // Sort by the ID in ascending order
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();
        } else {
            // If a genre is specified, filter by genre and order by ID (ascending)
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("genres", "ilike." + genre) // Filter by genre
                            .queryParam("order", "id.asc") // Sort by the ID in ascending order
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