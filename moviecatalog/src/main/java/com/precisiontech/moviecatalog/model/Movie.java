package com.precisiontech.moviecatalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {

    private String title;
    private String releaseDate;
    private String pg_rating;
    private String synopsis;
    private String genres;
    private String production_companies;
    private int runtime;
    private String spoken_languages;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("movie_id")
    private String movieId;

    public Movie(String title, String releaseDate, String posterPath, String genres, String synopsis, String pg_rating, String production_companies, int runtime, String spoken_languages) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.genres = genres;
        this.synopsis = synopsis;
        this.pg_rating = pg_rating;
        this.production_companies = production_companies;
        this.runtime = runtime;
        this.spoken_languages = spoken_languages;
    }

    // ------------- Getters and Setters -------------


    /**
     * Gets the movie title.
     *
     * @return the title as a String
     */
    public String getTitle() {return title;}


    /**
     * Sets the movie title.
     *
     * @param title      the new title
     */
    public void setTitle(String title) {this.title = title;}


    /**
     * Gets the movie's release date.
     *
     * @return the release date as a {@link String}
     */
    @JsonProperty("release_date")
    public String getReleaseDate() {return releaseDate;}


    /**
     * Sets the movie's release date.
     *
     * @param releaseDate      the new release date
     */
    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}


    /**
     * Gets the movie's pg rating.
     *
     * @return the pg rating as a String
     */
    public String getPgRating() {return pg_rating;}


    /**
     * Sets the movie's pg rating.
     *
     * @param pg_rating      the new pg rating
     */
    public void setPgRating(String pg_rating) {this.pg_rating = pg_rating;}


    /**
     * Gets the movie synopsis.
     *
     * @return the synopsis as a String
     */
    public String getSynopsis() {return synopsis;}


    /**
     * Sets the movie synopsis.
     *
     * @param synopsis      the new synopsis
     */
    public void setSynopsis(String synopsis) {this.synopsis = synopsis;}


    /**
     * Gets the movie's genres.
     *
     * @return the genres as a String
     */
    public String getGenres() {return genres;}


    /**
     * Sets the movie's genres.
     *
     * @param genres      the new genres
     */
    public void setGenres(String genres) {this.genres = genres;}


    /**
     * Gets the production companies who made the movie.
     *
     * @return the production companies as a String
     */
    public String getProductionCompanies() {return production_companies;}


    /**
     * Sets the production companies of the movie.
     *
     * @param production_companies      the new production companies
     */
    public void setProductionCompanies(String production_companies) {this.production_companies = production_companies;}


    /**
     * Gets the movie runtime.
     *
     * @return the runtime as an Integer
     */
    public int getRuntime() {return runtime;}


    /**
     * Sets the movie runtime.
     *
     * @param runtime      the new runtime
     */
    public void setRuntime(int runtime) {this.runtime = runtime;}


    /**
     * Gets the movie's spoken languages.
     *
     * @return the spoken languages as a String
     */
    public String getSpokenLanguages() {return spoken_languages;}


    /**
     * Sets the movie's spoken languages.
     *
     * @param spoken_languages      the new spoken languages
     */
    public void setSpokenLanguages(String spoken_languages) {this.spoken_languages = spoken_languages;}


    /**
     * Gets the path to the movie's cover image.
     *
     * @return the path as a String
     */
    public String getPosterPath() {return posterPath;}


    /**
     * Sets the path to the movie's cover image.
     *
     * @param posterPath      the new path
     */
    public void setPosterPath(String posterPath) {this.posterPath = posterPath;}


    /**
     * Gets the movie id.
     *
     * @return the id as a String
     */
    public String getMovieId() {return movieId;}


    /**
     * Sets the movie id.
     *
     * @param movieId      the new id
     */
    public void setMovieId(String movieId) {this.movieId = movieId;}

}