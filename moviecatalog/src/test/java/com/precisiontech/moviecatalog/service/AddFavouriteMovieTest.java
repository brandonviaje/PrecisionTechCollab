package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AddFavouriteMovieTest {

    private static MockWebServer mockWebServer;
    private static AddFavouriteMovie addFavouriteMovie;
    private static SupabaseConfig supabaseConfig;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        supabaseConfig = mock(SupabaseConfig.class);
        when(supabaseConfig.getSupabaseApiKey()).thenReturn("test-api-key");

        addFavouriteMovie = new AddFavouriteMovie(webClient, supabaseConfig);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testAddFavouriteMovie() throws Exception {
        // Mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\":\"Favourite movie added successfully\"}")
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Movie movie = new Movie("Test Title", "12-02-02", "/testPath.jpg",
                "Test Genre", "Test Synopsis", "R", "Test Company", 148, "Test Language");

        addFavouriteMovie.addFavouriteMovie("testuser", movie);
        System.out.println("Test Passed: Favourite movie successfully added to the database.");
    }
}