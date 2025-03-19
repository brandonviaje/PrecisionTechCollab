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

    /**
     * Adds a movie to the movies database
     *
     * @param movie         the movie to be added
     */
    public void addMovie(Movie movie) {
        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", movie.getTitle());
        movieData.put("release_date", movie.getReleaseDate());
        movieData.put("poster_path", movie.getPosterPath());
        movieData.put("genres", movie.getGenres());
        movieData.put("synopsis", movie.getSynopsis());

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

    /**
     * Fetches a movie by its id
     *
     * @param movieId           the id of the movie
     * @return                  the movie being fetched
     */
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

                // create new movie object with retrieved details
                movie = new Movie(title, releaseDate, posterPath, genres, synopsis);
                movie.setMovieId(movieId);
            }
        } catch (Exception e) {
            System.err.println("Error fetching movie by ID: " + e.getMessage());
        }
        return movie;
    }

    /**
     * Saves the image cover of the movie entered by the user to the 'userimg' folder
     *
     * @param poster            the image file to be saved
     * @return                  the path to image
     */
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

    /**
     * Fetches movies from the database by the specified genre
     *
     * @param genre         genre of the movie
     * @return              movie(s) attached to the specified genre
     */
    public List<Movie> filterByGenre(String genre) {
        List<Movie> allMovies;

        //if not genres then show all movies
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
        //if genre is found, show those specific movies
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

    /**
     * Fetches movies from the database by the specified pgrating
     *
     * @param pgRating         pgrating of the movie
     * @return              movie(s) attached to the specified pgrating
     */

    public List<Movie> filterByPgRating (String pgRating){
        List <Movie> allMovies;

        //if no pgrating is found, show all movies
        if(pgRating == null || pgRating.isEmpty()){
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
        }else{
        //if pgrating is found, show those movies
            allMovies = webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/rest/v1/movies")
                    .queryParam("pg_rating", "eq." + pgRating)
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
    
     /**
     * Fetches movies from the database by the specified spoken language 
     *
     * @param language      spoken language in the movie
     * @return              movie(s) attached to the specified spoken language 
     */

    public List<Movie> filterByLanguage(String language){
        List <Movie> allMovies;

        //if no languages are found, show all movies
        if(language == null || language.isEmpty()){
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
        }else{
        //if found languages are found, show those movies
            allMovies = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                            .path("/rest/v1/movies")
                            .queryParam("language", "eq." + language)
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

    /**
     * Fetches movies from the database filtered by the title
     *
     * @param title         title of the movie
     * @return              movie(s) with the specified title
     */
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

    /**
     * Fetches all movies from the database.
     *
     * @return A list of all movies.
     */
    public List<Movie> getAllMovies() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/movies")
                        .queryParam("order", "id.asc") // Order by ID
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }

}
