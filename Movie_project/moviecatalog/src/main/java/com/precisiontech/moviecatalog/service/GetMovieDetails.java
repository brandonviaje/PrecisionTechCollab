package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GetMovieDetails {

    private final WebClient webClient;
    private final String supabaseApiKey;

    @Autowired
    public GetMovieDetails(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
        this.supabaseApiKey = supabaseConfig.getSupabaseApiKey();
    }


    /**
     * Fetches a movie by its ID.
     *
     * @param movieId           the ID of the movie
     * @return                  the movie being fetched
     */
    public Movie getMovieById(String movieId) {
        Movie movie = null;
        try {
            // Query Supabase for the movie based on movieId
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("movie_id", "eq." + movieId)  // Adjust the field name as per your Supabase table schema
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse response to retrieve the movie
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseNode = responseMapper.readTree(response);

            if (responseNode.isArray() && !responseNode.isEmpty()) {
                JsonNode movieNode = responseNode.get(0);

                // Extract fields from Supabase response
                String title = movieNode.get("title").asText();
                String releaseDate = movieNode.get("release_date").asText();
                String posterPath = movieNode.get("poster_path").asText();
                String genres = movieNode.get("genres").asText();
                String synopsis = movieNode.get("synopsis").asText();
                String pgRating = movieNode.get("pg_rating").asText();
                String productionCompanies = movieNode.get("production_companies").asText();
                int runtime = movieNode.get("runtime").asInt();
                String spokenLanguages = movieNode.get("spoken_languages").asText();

                // Create a new movie object with the retrieved details
                movie = new Movie(title, releaseDate, posterPath, genres, synopsis, pgRating, productionCompanies, runtime, spokenLanguages);
                movie.setMovieId(movieId);  // Set the movie ID
            }
        } catch (Exception e) {
            System.err.println("Error fetching movie by ID: " + e.getMessage());
        }
        return movie;
    }
}
