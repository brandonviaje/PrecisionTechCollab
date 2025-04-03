package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetMovieDetailsTest {

    private GetMovieDetails getMovieDetails;
    private MockWebServer mockWebServer;

    @Mock
    private Builder webClientBuilder;  // Mocking WebClient.Builder

    private SupabaseConfig supabaseConfig;

    @BeforeEach
    void setUp() throws Exception {
        // Start MockWebServer to simulate Supabase API
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create a mock for the WebClient.Builder
        webClientBuilder = org.mockito.Mockito.mock(WebClient.Builder.class);

        WebClient mockWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .defaultHeader("apikey", "fake-api-key")
                .build();

        when(webClientBuilder.baseUrl(mockWebServer.url("/").toString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mockWebClient);

        // initialize SupabaseConfig with mock values
        supabaseConfig = new SupabaseConfig() {
            @Override
            public String getSupabaseUrl() {return mockWebServer.url("/").toString();}

            @Override
            public String getSupabaseApiKey() {return "fake-api-key";}
        };

        // initialize GetMovieDetails service with mock WebClient and SupabaseConfig
        getMovieDetails = new GetMovieDetails(webClientBuilder, supabaseConfig);
    }

    @Test
    void testGetMovieById() throws Exception {
        // mock response form movie details
        String movieResponse = "[{" +
                "\"movie_id\": \"1\"," +
                "\"title\": \"Inception\"," +
                "\"release_date\": \"2010-07-16\"," +
                "\"poster_path\": \"/path/to/poster.jpg\"," +
                "\"genres\": \"Action, Sci-Fi\"," +
                "\"synopsis\": \"A mind-bending thriller.\"," +
                "\"pg_rating\": \"PG-13\"," +
                "\"production_companies\": \"Warner Bros.\"," +
                "\"runtime\": 148," +
                "\"spoken_languages\": \"English\"" +
                "}]";

        // enqueue the mock response
        mockWebServer.enqueue(new MockResponse().setBody(movieResponse).setResponseCode(200));

        // call method with a valid movie ID
        Movie movie = getMovieDetails.getMovieById("1");

        // Assert that the movie details are correctly parsed
        assertNotNull(movie);
        assertEquals("Inception", movie.getTitle());
        assertEquals("2010-07-16", movie.getReleaseDate());
        assertEquals("/path/to/poster.jpg", movie.getPosterPath());
        assertEquals("Action, Sci-Fi", movie.getGenres());
        assertEquals("A mind-bending thriller.", movie.getSynopsis());
        assertEquals("PG-13", movie.getPgRating());
        assertEquals("Warner Bros.", movie.getProductionCompanies());
        assertEquals(148, movie.getRuntime());
        assertEquals("English", movie.getSpokenLanguages());
        assertEquals("1", movie.getMovieId());
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
