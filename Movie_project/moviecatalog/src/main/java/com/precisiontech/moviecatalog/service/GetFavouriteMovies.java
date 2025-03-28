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
import java.util.Map;

@Service
public class GetFavouriteMovies {

    private final WebClient webClient;
    private final String supabaseApiKey;

    @Autowired
    public GetFavouriteMovies(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
    }

    public List<Movie> getFavouriteMovieByUsername(String username) {
        List<Movie> userFavourites = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/favourites")
                        .queryParam("username", "eq." + username)
                        .queryParam("order", "id.asc")
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Map.class)  // Retrieve as a Map first
                .map(movieMap -> {
                    // Explicitly convert Map to Movie object
                    Movie movie = new Movie(
                            (String) movieMap.get("title"),
                            (String) movieMap.get("release_date"),
                            (String) movieMap.get("poster_path"),
                            (String) movieMap.get("genres"),
                            (String) movieMap.get("synopsis"),
                            (String) movieMap.get("pg_rating"),
                            (String) movieMap.get("production_companies"),
                            movieMap.get("runtime") instanceof Integer ? (Integer) movieMap.get("runtime") : 0,
                            (String) movieMap.get("spoken_languages")
                    );

                    // Explicitly set the movie ID
                    movie.setMovieId((String) movieMap.get("movie_id"));

                    // Debug logging
                    System.out.println("Mapped Movie: " + movie);
                    System.out.println("Movie ID: " + movie.getMovieId());

                    return movie;
                })
                .collectList()
                .block();

        return userFavourites;
    }
}
