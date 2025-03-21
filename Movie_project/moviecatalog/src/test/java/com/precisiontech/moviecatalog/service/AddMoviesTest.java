package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddMoviesTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private AddMovies addMoviesService;
    private SupabaseConfig supabaseConfig;

    @BeforeEach
    public void setUp() throws Exception {
        // Start MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create WebClient instance pointing to the MockWebServer URL
        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        // Initialize SupabaseConfig and set the API key
        supabaseConfig = new SupabaseConfig();
        setApiKey(supabaseConfig);

        // Initialize AddMovies service
        addMoviesService = new AddMovies(webClient, supabaseConfig);
    }

    private void setApiKey(SupabaseConfig config) throws Exception {
        var field = SupabaseConfig.class.getDeclaredField("supabaseApiKey");
        field.setAccessible(true);
        field.set(config, "test-api-key");
    }

    @Test
    public void testAddMovie() throws Exception {
        // Create test movie
        Movie movie = new Movie("Test Title", "12-02-02", "/testPath.jpg",
                "Test Genre", "Test Synopsis", "R", "Test Company", 148, "Test Language");

        // Mock HTTP response from Supabase
        mockWebServer.enqueue(new MockResponse()
                .setBody("[{\"movie_id\":\"12345\"}]")
                .addHeader("Content-Type", "application/json"));

        // Call the method to add movie
        addMoviesService.addMovie(movie);

        // Validate that the movie ID was set correctly
        assertEquals("12345", movie.getMovieId());

        // Verify request details
        var request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/rest/v1/movies", request.getPath());
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("test-api-key", request.getHeader("apikey"));

        // Verify request body contains expected data
        ObjectMapper mapper = new ObjectMapper();
        JsonNode requestBody = mapper.readTree(request.getBody().readUtf8());
        assertEquals("Test Title", requestBody.get("title").asText());
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}