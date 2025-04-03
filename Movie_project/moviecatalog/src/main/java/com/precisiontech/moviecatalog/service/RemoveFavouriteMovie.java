package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Service
public class RemoveFavouriteMovie {
    private final WebClient webClient;
    private final SupabaseConfig supabaseConfig;
    private final GetMovieDetails movieDetails; // Add this dependency

    @Autowired
    public RemoveFavouriteMovie(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig, GetMovieDetails movieDetails) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseConfig = supabaseConfig;
        this.movieDetails = movieDetails; // Inject the MovieDetails service
    }

    private String getMovieTitleById(String movieId) {
        try {
            // Use the existing GetMovieDetails service to fetch the movie
            Movie movie = movieDetails.getMovieById(movieId);

            if (movie != null) {
                return movie.getTitle();
            } else {
                System.err.println("No movie found with ID: " + movieId);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching movie title for ID: " + movieId);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Removes a movie from the user's favourites list.
     *
     * @param username      the user's username
     * @param movieId       the movie id
     * @return a message indicating whether the movie was removed
     */
    public boolean removeFavouriteMovie(String username, String movieId) {
        try {
            Long deletedCount = webClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/favourites")
                            .queryParam("username", "eq." + username)
                            .queryParam("movie_id", "eq." + movieId)  // Use movie_id instead of internal ID
                            .build())
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Prefer", "return=representation")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();

            return deletedCount != null && deletedCount > 0;
        } catch (Exception e) {
            System.err.println("Error removing favorite movie: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}