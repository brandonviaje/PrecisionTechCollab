package com.precisiontech.moviecatalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {

    private String title;
    private String releaseDate;
    private String pgRating;
    private String synopsis;
    private String genres;
    private String productionCompanies;
    private int runtime;
    private String spokenLanguages;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("movie_id")
    private String movieId;

    public Movie(String title, String releaseDate, String posterPath) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
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
        return pgRating;
    }

    public void setPgRating(String pgRating) {
        this.pgRating = pgRating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getGenres() {return genres;}

    public void setGenres(String genres) {this.genres = genres;}

    public String getProductionCompanies() {return productionCompanies;}

    public void setProductionCompanies(String productionCompanies) {this.productionCompanies = productionCompanies;}

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getPosterPath() {return posterPath;}

    public void setPosterPath(String posterPath) {this.posterPath = posterPath;}

    public String getMovieId() {return movieId;}

    public void setMovieId(String movieId) {this.movieId = movieId;}

}