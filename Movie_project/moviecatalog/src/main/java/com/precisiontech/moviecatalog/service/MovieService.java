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
        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", movie.getTitle());
        movieData.put("release_date", movie.getReleaseDate());
        movieData.put("poster_path", movie.getPosterPath());
        movieData.put("genres", movie.getGenres());
        movieData.put("synopsis", movie.getSynopsis());
        movieData.put("pg_rating",movie.getPgRating());
        movieData.put("runtime", movie.getRuntime());
        movieData.put("production_companies", movie.getProductionCompanies());
        movieData.put("spoken_languages", movie.getSpokenLanguages());

        try {
            // Serialize the data into JSON format
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(movieData);

            // Insert movie into Supabase
            String response = webClient.post()
                    .uri("/rest/v1/movies")
                    .header("apikey", supabaseApiKey)
                    .header("Prefer", "return=representation") // Ensures the response returns the inserted record
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse the response to get the new movie_id
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseNode = responseMapper.readTree(response);
            String movieId = responseNode.get(0).get("movie_id").asText();

            System.out.println("Movie added with ID: " + movieId);
            movie.setMovieId(movieId);

        } catch (Exception e) {
            System.err.println("Error adding movie: " + e.getMessage());
        }
    }

    public Movie getMovieById(String movieId) {
        Movie movie = null;
        try {
            // query Supabase for the movie based on movieId
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("movie_id", "eq." + movieId)
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            //parse response to retrieve the movie
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseNode = responseMapper.readTree(response);

            if (responseNode.isArray() && !responseNode.isEmpty()) {
                JsonNode movieNode = responseNode.get(0);
                //extract field from supabase
                String title = movieNode.get("title").asText();
                String releaseDate = movieNode.get("release_date").asText();
                String posterPath = movieNode.get("poster_path").asText();
                String genres = movieNode.get("genres").asText();
                String synopsis = movieNode.get("synopsis").asText();
                String pgRating = movieNode.get("pg_rating").asText();
                String productionCompanies = movieNode.get("production_companies").asText();
                int runtime = movieNode.get("runtime").asInt();
                String spokenLanguages = movieNode.get("spoken_languages").asText();

                // create new movie object with retrieved details
                movie = new Movie(title, releaseDate, posterPath, genres, synopsis, pgRating, productionCompanies, runtime, spokenLanguages);
                movie.setMovieId(movieId);
            }
        } catch (Exception e) {
            System.err.println("Error fetching movie by ID: " + e.getMessage());
        }
        return movie;
    }

    // save the image to the 'userimg' folder and return the relative path
    public String saveImage(MultipartFile poster) {
        try {
            // Take file name
            String imageName = poster.getOriginalFilename();
            Path path = Paths.get("src", "main", "resources", "static", "userimg", imageName); // Store the image inside the userimg folder
            Files.createDirectories(path.getParent());      // Create the directory if it doesn't exist
            poster.transferTo(path);           // Save the file to the specified path
            return "/userimg/" + imageName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage());
        }
    }

    public List<Movie> getAllMovies() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/movies")
                        .queryParam("order", "id.asc") //the first 50 movies in database
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }

    public List<Movie> filterByGenre(String genre) {
        if (genre == null) {
            return getAllMovies();
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/movies")
                        .queryParam("genres", "ilike.%" + genre + "%")
                        .queryParam("order", "id.asc")
                        .build(genre)) // Pass genre separately to avoid null issues
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
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

    public void updateMovieDetails(String movieId, Movie updatedMovie) {
        Map<String, Object> updateFields = new HashMap<>();

        // Add fields to update, checking for null values
        if (updatedMovie.getTitle() != null) {updateFields.put("title", updatedMovie.getTitle());}
        if (updatedMovie.getReleaseDate() != null) {updateFields.put("release_date", updatedMovie.getReleaseDate());}
        if (updatedMovie.getGenres() != null) {updateFields.put("genres", updatedMovie.getGenres());}
        if (updatedMovie.getSynopsis() != null) {updateFields.put("synopsis", updatedMovie.getSynopsis());}
        if (updatedMovie.getRuntime() != 0) {  updateFields.put("runtime", updatedMovie.getRuntime());}
        if (updatedMovie.getSpokenLanguages() != null) {updateFields.put("spoken_languages", updatedMovie.getSpokenLanguages());}
        if (updatedMovie.getProductionCompanies() != null) {updateFields.put("production_companies", updatedMovie.getProductionCompanies());}
        if (updatedMovie.getPgRating() != null) {updateFields.put("pg_rating", updatedMovie.getPgRating());}
        if (updatedMovie.getPosterPath() != null) {updateFields.put("poster_path", updatedMovie.getPosterPath());}

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(updateFields);

            // Send PATCH request to update movie details in the database
            webClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("movie_id", "eq." + movieId)
                            .build())
                    .header("apikey", supabaseApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error updating movie: " + e.getMessage(), e);
        }
    }
}
