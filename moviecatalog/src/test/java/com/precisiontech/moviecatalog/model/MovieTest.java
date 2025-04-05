package com.precisiontech.moviecatalog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    private Movie movie;

    @BeforeEach
    public void setUp() {
        // Initialize the Movie object before each test
        movie = new Movie("Inception", "2010-07-16", "/poster_path.jpg",
                "Action, Adventure", "A mind-bending thriller",
                "PG-13", "Warner Bros.", 148, "English");
    }

    @Test
    public void testConstructorAndGetters() {
        // Testing constructor and getters
        assertEquals("Inception", movie.getTitle());
        assertEquals("2010-07-16", movie.getReleaseDate());
        assertEquals("/poster_path.jpg", movie.getPosterPath());
        assertEquals("Action, Adventure", movie.getGenres());
        assertEquals("A mind-bending thriller", movie.getSynopsis());
        assertEquals("PG-13", movie.getPgRating());
        assertEquals("Warner Bros.", movie.getProductionCompanies());
        assertEquals(148, movie.getRuntime());
        assertEquals("English", movie.getSpokenLanguages());
    }

    @Test
    public void testSetters() {
        // Testing setters
        movie.setTitle("The Dark Knight");
        movie.setReleaseDate("2008-07-18");
        movie.setPosterPath("/dark_knight_poster.jpg");
        movie.setGenres("Action, Crime, Drama");
        movie.setSynopsis("Batman faces off against the Joker");
        movie.setPgRating("PG-13");
        movie.setProductionCompanies("Warner Bros.");
        movie.setRuntime(152);
        movie.setSpokenLanguages("English");

        // Verifying the updated values using getters
        assertEquals("The Dark Knight", movie.getTitle());
        assertEquals("2008-07-18", movie.getReleaseDate());
        assertEquals("/dark_knight_poster.jpg", movie.getPosterPath());
        assertEquals("Action, Crime, Drama", movie.getGenres());
        assertEquals("Batman faces off against the Joker", movie.getSynopsis());
        assertEquals("PG-13", movie.getPgRating());
        assertEquals("Warner Bros.", movie.getProductionCompanies());
        assertEquals(152, movie.getRuntime());
        assertEquals("English", movie.getSpokenLanguages());
    }

    @Test
    public void testMovieIdSetter() {
        // Testing only the setter for movieId
        movie.setMovieId("12345");
        assertEquals("12345", movie.getMovieId());
    }

    @Test
    public void testNullValues() {
        // Testing when some values are set to null
        Movie movieWithNullValues = new Movie(null, null, null, null, null, null, null, 0, null);

        assertNull(movieWithNullValues.getTitle());
        assertNull(movieWithNullValues.getReleaseDate());
        assertNull(movieWithNullValues.getPosterPath());
        assertNull(movieWithNullValues.getGenres());
        assertNull(movieWithNullValues.getSynopsis());
        assertNull(movieWithNullValues.getPgRating());
        assertNull(movieWithNullValues.getProductionCompanies());
        assertEquals(0, movieWithNullValues.getRuntime());
        assertNull(movieWithNullValues.getSpokenLanguages());
    }
}
