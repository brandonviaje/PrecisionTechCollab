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
    private final FetchMovies movieFetchService;

    @Autowired
    public FilterMovies(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig, FetchMovies movieFetchService) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
        this.movieFetchService = movieFetchService;
    }

    /**
     * Fetches movies based on a given query parameter.
     *
     * @param queryParam   the query parameter to filter movies
     * @param paramValue   the value to filter by (e.g., genre, pg_rating, language)
     * @return             a list of filtered movies
     */
    private List<Movie> fetchMoviesByQuery(String queryParam, String paramValue) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/movies")
                        .queryParam(queryParam, "ilike.%" + paramValue + "%")
                        .queryParam("order", "title.asc")
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }

    /**
     * Fetches movies from the database by the specified genre
     *
     * @param genre         genre of the movie
     * @return              movie(s) attached to the specified genre
     */
    public List<Movie> filterByGenre(String genre) {
        return genre == null ? movieFetchService.getAllMovies() : fetchMoviesByQuery("genres", genre);
    }

    /**
     * Fetches movies from the database by the specified pg_rating
     *
     * @param pgRating      pg_rating of the movie
     * @return              movie(s) attached to the specified pg_rating
     */
    public List<Movie> filterByPgRating(String pgRating) {
        return pgRating == null ? movieFetchService.getAllMovies() : fetchMoviesByQuery("pg_rating", pgRating);
    }

    /**
     * Fetches movies from the database by the specified spoken language
     *
     * @param language      spoken language in the movie
     * @return              movie(s) attached to the specified spoken language
     */
    public List<Movie> filterByLanguage(String language) {
        return language == null ? movieFetchService.getAllMovies() : fetchMoviesByQuery("spoken_languages", language);
    }

    /**
     * Fetches movies from the database by the specified spoken language
     *
     * @param genre,pgRating,languages     tags associated with the movie
     * @return              movie(s) attached to the specified tags
     */

    public List<Movie> filterMovies(String genre, String pgRating, String language) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/rest/v1/movies");
    
                    if (genre != null && !genre.isEmpty()) {
                        uriBuilder.queryParam("genres", "ilike.%" + genre + "%");
                    }
                    if (pgRating != null && !pgRating.isEmpty()) {
                        uriBuilder.queryParam("pg_rating", "ilike.%" + pgRating + "%");
                    }
                    if (language != null && !language.isEmpty()) {
                        uriBuilder.queryParam("spoken_languages", "ilike.%" + language + "%");
                    }
    
                    uriBuilder.queryParam("order", "title.asc");
                    return uriBuilder.build();
                })
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }
    
}