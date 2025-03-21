package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddMoviesTest {

    @Mock
    private WebClient webClient;

    @Mock
    private SupabaseConfig supabaseConfig;

    @InjectMocks
    private AddMovies addMovieService;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddMovie() throws Exception {
        Movie movie = new Movie("Test Title", "12-02-02", "/testPath.jpg", "Test Genre", "Test Synopsis", "R", "Test Company", 148, "Test Language");

        // Mock Supabase API key
        when(supabaseConfig.getSupabaseApiKey()).thenReturn("test-api-key");

        // Mock WebClient behavior
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/rest/v1/movies")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq("apikey"), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq("Prefer"), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq("Content-Type"), anyString())).thenReturn(requestBodyUriSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestBodyUriSpec.bodyValue(any(String.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just("[{\"movie_id\":\"12345\"}]"));

        // Call the method
        addMovieService.addMovie(movie);

        // Verify account ID is set correctly
        assert movie.getMovieId().equals("12345");

        // Verify WebClient interactions
        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri("/rest/v1/movies");
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }
}