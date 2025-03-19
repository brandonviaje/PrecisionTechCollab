package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class FilterMovies {

    private final WebClient webClient;
    private final String supabaseApiKey;
    private final FetchMovies movieFetchService;  // Injecting MovieFetchService

    @Autowired
    public FilterMovies(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig, FetchMovies movieFetchService) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
        this.movieFetchService = movieFetchService;
    }

    /**
     * Fetches movies from the database by the specified genre
     *
     * @param genre         genre of the movie
     * @return              movie(s) attached to the specified genre
     */
    public List<Movie> filterByGenre(String genre) {
        List<Movie> allMovies;

        if (genre == null) {
            return movieFetchService.getAllMovies();
        } else {
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("genres", "ilike.%" + genre + "%")
                            .queryParam("order", "title.asc")
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

    /**
     * Fetches movies from the database by the specified pg_rating
     *
     * @param pgRating         pg_rating of the movie
     * @return                 movie(s) attached to the specified pg_rating
     */
    public List<Movie> filterByPgRating(String pgRating) {
        List<Movie> allMovies;

        if (pgRating == null) {
            return movieFetchService.getAllMovies();
        } else {
            // If rating is provided, fetch those specific movies
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("pg_rating", "ilike.%" + pgRating + "%")
                            .queryParam("order", "title.asc")
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

    /**
     * Fetches movies from the database by the specified spoken language
     *
     * @param language      spoken language in the movie
     * @return              movie(s) attached to the specified spoken language
     */
    public List<Movie> filterByLanguage(String language) {
        List<Movie> allMovies;

        if (language == null) {
            return movieFetchService.getAllMovies();
        } else {
            // If language is provided, fetch those specific movies
            allMovies = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("spoken_languages", "ilike.%" + language + "%")
                            .queryParam("order", "title.asc")
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
}
