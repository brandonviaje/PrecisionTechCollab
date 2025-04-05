package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchMoviesTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private SupabaseConfig supabaseConfig;
    private SearchMovies searchMoviesService;
    private FetchMovies fetchMoviesService;
    private MovieFetcher movieFetcher;

    @BeforeEach
    public void setUp() throws Exception {
        // Start MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // initialize SupabaseConfig and set API Key
        supabaseConfig = new SupabaseConfig();
        setApiKey(supabaseConfig, "test-api-key");
        setSupabaseUrl(supabaseConfig, mockWebServer.url("/").toString());

        webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        movieFetcher = () -> List.of(new Movie("Mock Movie", "2023-01-01", "/mock.jpg", "Mock Genre", "Mock Synopsis", "PG", "Mock Studio", 100, "English"));


        fetchMoviesService = new FetchMovies(movieFetcher);
        searchMoviesService = new SearchMovies(WebClient.builder(), supabaseConfig, fetchMoviesService);
    }

    private void setApiKey(SupabaseConfig config, String apiKey) throws Exception {
        var field = SupabaseConfig.class.getDeclaredField("supabaseApiKey");
        field.setAccessible(true);
        field.set(config, apiKey);
    }

    private void setSupabaseUrl(SupabaseConfig config, String url) throws Exception {
        var field = SupabaseConfig.class.getDeclaredField("supabaseUrl");
        field.setAccessible(true);
        field.set(config, url);
    }

    @Test
    public void testSearchMoviesByTitle() throws Exception {
        // Sample JSON response
        String jsonResponse = """
                [
                    {
                        "id": 1,
                        "title": "Test Movie",
                        "release_date": "2022-01-01",
                        "poster_path": "/test.jpg",
                        "genres": "Action",
                        "synopsis": "Test synopsis",
                        "pg_rating": "PG-13",
                        "production_companies": "Test Studio",
                        "runtime": 120,
                        "spoken_languages": "English"
                    }
                ]
                """;

        // Mock HTTP response from Supabase
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        // Call searchMovies method
        List<Movie> result = searchMoviesService.searchMovies("Test Movie");

        // Verify the response
        assertEquals(1, result.size());
        Movie movie = result.get(0);
        assertEquals("Test Movie", movie.getTitle());
        assertEquals("2022-01-01", movie.getReleaseDate());

        // Verify HTTP request details
        var request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/rest/v1/movies?title=ilike.%25Test%20Movie%25&order=id.asc", request.getPath());
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("test-api-key", request.getHeader("apikey"));
    }

    @Test
    public void testSearchMoviesNoTitleReturnsAll() {
        // Call searchMovies with an empty title, expecting FetchMovies to return a mocked list
        List<Movie> result = searchMoviesService.searchMovies("");

        // Verify that it returns our mock movie
        assertEquals(1, result.size());
        assertEquals("Mock Movie", result.get(0).getTitle());
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
