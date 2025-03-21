package com.precisiontech.moviecatalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.precisiontech.moviecatalog.model.Movie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.lang.reflect.Field;

import org.skyscreamer.jsonassert.JSONAssert;  // Import JSONAssert

class EditMoviesTest {

    private MockWebServer mockWebServer;
    private EditMovies editMovies;

    @BeforeEach
    void setUp() throws Exception {
        // Start MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create an instance of EditMovies
        editMovies = new EditMovies();

        // Use reflection to inject private values (e.g., supabaseUrl, supabaseApiKey)
        Field urlField = EditMovies.class.getDeclaredField("supabaseUrl");
        urlField.setAccessible(true);
        urlField.set(editMovies, mockWebServer.url("/").toString()); // Inject the mock server URL

        Field apiKeyField = EditMovies.class.getDeclaredField("supabaseApiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(editMovies, "test-api-key"); // Inject mock API key

        // Initialize WebClient with the injected URL
        editMovies.init();
    }

    @Test
    void testUpdateMovieDetails() throws Exception {
        //sample updated movie
        Movie updatedMovie = new Movie("Titanic", "1997-12-19", "/titanic.jpg", "Romance", "Ship sinking", "PG-13", "Fox", 195, "English");
        updatedMovie.setTitle("Updated Title");
        updatedMovie.setReleaseDate("2022-12-12");
        updatedMovie.setGenres("Action, Adventure");
        updatedMovie.setRuntime(120);
        updatedMovie.setSpokenLanguages("English");
        updatedMovie.setProductionCompanies("Test Production");
        updatedMovie.setPgRating("PG-13");
        updatedMovie.setPosterPath("/path/to/poster.jpg");

        // Prepare a mock response from the server
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"message\":\"Movie updated successfully\"}"));

        // Call the method to update the movie details
        editMovies.updateMovieDetails("12345", updatedMovie);

        // Capture the request sent to MockWebServer
        RecordedRequest request = mockWebServer.takeRequest();

        // Assert the URL path includes the correct movie ID
        assertThat(request.getPath()).isEqualTo("/rest/v1/movies?movie_id=eq.12345");

        // Prepare the expected request body
        String expectedRequestBody = "{\"title\":\"Updated Title\",\"release_date\":\"2022-12-12\",\"genres\":\"Action, Adventure\",\"runtime\":120,\"spoken_languages\":\"English\",\"production_companies\":\"Test Production\",\"pg_rating\":\"PG-13\",\"poster_path\":\"/path/to/poster.jpg\",\"synopsis\":\"Ship sinking\"}";
        JSONAssert.assertEquals(expectedRequestBody, request.getBody().readUtf8(), false);
    }
}
