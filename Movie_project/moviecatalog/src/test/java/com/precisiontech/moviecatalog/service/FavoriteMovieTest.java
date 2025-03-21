package com.precisiontech.moviecatalog.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

//Our Red Test: Implementing in Iteration 3.
public class FavoriteMovieTest {
    @Test
    public void testAddFavoriteMovie_EmptyMovieName() {
        // Given an empty movie name
        String movieName = "";

        //adding the movie
        boolean isAdded =  FavoriteMovie.addFavoriteMovie(movieName);
        //the movie should not be added, and the operation should fail
        assertThat(isAdded).isFalse();
    }

    @Test
    public void testAddFavoriteMovie_ValidMovieName() {
        // Given a valid movie name
        String movieName = "Inception";

        //adding the movie
        boolean isAdded = FavoriteMovie.addFavoriteMovie(movieName);
        //the movie should be added successfully
        assertThat(isAdded).isTrue();
    }

    @Test
    public void testAddFavoriteMovie_NullMovieName() {
        // Given a null movie name
        String movieName = null;

        // When adding the movie
        boolean isAdded = FavoriteMovie.addFavoriteMovie(movieName);
        // the movie should not be added
        assertThat(isAdded).isFalse();
    }
}
