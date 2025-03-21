package com.precisiontech.moviecatalog.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DeleteMoviesTest {

    private MockWebServer mockWebServer;
    private DeleteMovies deleteMoviesService;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    @BeforeEach
    public void setUp() throws Exception {
        // Start the MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Initialize DeleteMovies with the MockWebServer URL
        deleteMoviesService = new DeleteMovies();
        deleteMoviesService.supabaseUrl = mockWebServer.url("/").toString();
        deleteMoviesService.supabaseApiKey = supabaseApiKey;
        deleteMoviesService.init();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testDeleteMovieSuccess() {
        // mocking response for a successful movie deletion
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));
        boolean result = deleteMoviesService.deleteMovie("123");
        assertThat(result).isTrue();
    }

    @Test
    public void testDeleteMovieNotFound() {
        // mocking response for movie not found (HTTP 404 Not Found)
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        boolean result = deleteMoviesService.deleteMovie("123");
        assertThat(result).isFalse();
    }

}
