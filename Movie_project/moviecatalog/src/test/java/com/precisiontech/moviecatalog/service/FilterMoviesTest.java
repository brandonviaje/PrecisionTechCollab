package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesTest {

    private MockWebServer mockWebServer;
    private FilterMovies filterMovies;

    @BeforeEach
    void setUp() throws IOException {
        // Start MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        SupabaseConfig supabaseConfig = new SupabaseConfig() {
            @Override
            public String getSupabaseUrl() {
                return mockWebServer.url("/").toString();
            }

            @Override
            public String getSupabaseApiKey() {
                return "test-api-key";
            }
        };

        WebClient webClient = WebClient.builder().baseUrl(supabaseConfig.getSupabaseUrl()).build();

        FetchMovies fetchMovies = new FetchMovies(() -> webClient.get()
                .uri("/rest/v1/movies")
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block());

        filterMovies = new FilterMovies(WebClient.builder(), supabaseConfig, fetchMovies);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testFilterByGenre() throws Exception {
        List<Movie> mockMovies = List.of(
                new Movie("Inception", "2010-07-16", "/inception.jpg", "Sci-Fi", "Dreams within dreams", "PG-13", "WB", 148, "English"),
                new Movie("Interstellar", "2014-11-07", "/interstellar.jpg", "Sci-Fi", "Space travel", "PG-13", "Paramount", 169, "English")
        );

        String jsonResponse = new ObjectMapper().writeValueAsString(mockMovies);


        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Movie> movies = filterMovies.filterByGenre("Sci-Fi");

        assertNotNull(movies);
        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
        assertEquals("2010-07-16", movies.get(0).getReleaseDate());
        System.out.println("Test passed: Successfully filtered movies by genre. Found " + movies.size() + " Sci-Fi movies.");
    }

    @Test
    void testFilterByPgRating() throws Exception {
        List<Movie> mockMovies = List.of(
                new Movie("Avatar", "2009-12-18", "/avatar.jpg", "Action", "Blue aliens", "PG-13", "Fox", 162, "English")
        );

        String jsonResponse = new ObjectMapper().writeValueAsString(mockMovies);

        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Movie> movies = filterMovies.filterByPgRating("PG-13");

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("Avatar", movies.get(0).getTitle());
        assertEquals("2009-12-18", movies.get(0).getReleaseDate());
        System.out.println("Test passed: Successfully filtered movies by PG rating. Found " + movies.size() + " PG-13 movie(s).");
    }

    @Test
    void testFilterByLanguage() throws Exception {
        List<Movie> mockMovies = List.of(
                new Movie("Titanic", "1997-12-19", "/titanic.jpg", "Romance", "Ship sinking", "PG-13", "Fox", 195, "English")
        );

        String jsonResponse = new ObjectMapper().writeValueAsString(mockMovies);
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Movie> movies = filterMovies.filterByLanguage("English");

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("Titanic", movies.get(0).getTitle());
        assertEquals("1997-12-19", movies.get(0).getReleaseDate());
        System.out.println("Test passed: Successfully filtered movies by language. Found " + movies.size() + " English movie(s).");
    }
}
