package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RemoveFavouriteMovieTest {

    private static MockWebServer mockWebServer;
    private static RemoveFavouriteMovie removeFavouriteMovie;
    private static SupabaseConfig supabaseConfig;
    private static GetMovieDetails movieDetails;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        supabaseConfig = mock(SupabaseConfig.class);
        when(supabaseConfig.getSupabaseApiKey()).thenReturn("test-api-key");
        when(supabaseConfig.getSupabaseUrl()).thenReturn(mockWebServer.url("/").toString());

        movieDetails = mock(GetMovieDetails.class);
        when(movieDetails.getMovieById("123")).thenReturn(null); // Mock no movie found

        removeFavouriteMovie = new RemoveFavouriteMovie(WebClient.builder(), supabaseConfig, movieDetails);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testRemoveFavouriteMovie() {
        // Mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("1")
                .addHeader("Content-Type", "application/json"));

        boolean result = removeFavouriteMovie.removeFavouriteMovie("testuser", "123");
        assertTrue(result, "Movie should be removed successfully");
        System.out.println("Test Passed: Favourite movie successfully removed from the database.");
    }

    @Test
    void testRemoveFavouriteMovieNotFound() {
        // Mock response for no deletion
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("0")
                .addHeader("Content-Type", "application/json"));

        boolean result = removeFavouriteMovie.removeFavouriteMovie("testuser", "999");
        assertFalse(result, "Movie should not be removed as it doesn't exist");
        System.out.println("Test Passed: No favourite movie found to remove.");
    }
}
