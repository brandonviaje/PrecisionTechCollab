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

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPgRating() {
        return pg_rating;
    }

    public void setPgRating(String pg_rating) {
        this.pg_rating = pg_rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getGenres() {return genres;}

    public void setGenres(String genres) {this.genres = genres;}

    public String getProductionCompanies() {return production_companies;}

    public void setProductionCompanies(String production_companies) {this.production_companies = production_companies;}

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getSpokenLanguages() {
        return spoken_languages;
    }

    public void setSpokenLanguages(String spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getPosterPath() {return posterPath;}

    public void setPosterPath(String posterPath) {this.posterPath = posterPath;}

    public String getMovieId() {return movieId;}

    public void setMovieId(String movieId) {this.movieId = movieId;}

}