package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Component
public class WebClientMovieFetcher implements MovieFetcher {

    private final WebClient webClient;
    private final String supabaseApiKey;

    @Autowired
    public WebClientMovieFetcher(WebClient webClient, SupabaseConfig supabaseConfig) {
        this.webClient = webClient;
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
    }


    /**
     * Fetches a list of movies from the Supabase backend.
     *
     * @return a list of {@link Movie} objects fetched from the database
     */
    @Override
    public List<Movie> fetchMovies() {
        return webClient.get()
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
    }
}