package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
public class MovieService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    // Use Service Role Key (since RLS is disabled)
    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private WebClient webClient;
    private List<Movie> movies = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

    public void addMovie(Movie movie) {
        // Prepare for serializing the movie data
        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", movie.getTitle());
        movieData.put("release_date", movie.getReleaseDate());
        movieData.put("poster_path", movie.getPosterPath());
        movieData.put("genres", movie.getGenres());

        try {
            // Serialize the data into JSON format
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(movieData);

            // Insert movie into the database
            String response = webClient.post()
                    .uri("/rest/v1/movies")
                    .header("apikey", supabaseApiKey)
                    .header("Prefer", "return=representation") // Ensures the response returns the inserted record
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Assuming Supabase returns the inserted movie with its generated movie_id, parse the response
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseNode = responseMapper.readTree(response);
            String movieId = responseNode.get(0).get("movie_id").asText(); // Assuming the response contains movie_id

            System.out.println("Movie added with ID: " + movieId);

        } catch (Exception e) {
            System.err.println("Error adding movie: " + e.getMessage());
        }
    }

    // save the image to the 'userimg' folder and return the relative path
    public String saveImage(MultipartFile poster) {
        try {
            // Take file name
            String imageName = poster.getOriginalFilename();
            Path path = Paths.get("src", "main", "resources", "static", "userimg", imageName); // Store the image inside the userimg folder

            // Create the directory if it doesn't exist
            Files.createDirectories(path.getParent());

            // Save the file to the specified path
            poster.transferTo(path);

            // Return the relative path to the image (use forward slashes for URL compatibility)
            return "/userimg/" + imageName;

        } catch (IOException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage());
        }
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public List<Movie> filterByGenre(String genre) {
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

    public List<Movie> searchMovies(String title) {
        List<Movie> searchedMovies;
        // Return empty list if no title is provided
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
