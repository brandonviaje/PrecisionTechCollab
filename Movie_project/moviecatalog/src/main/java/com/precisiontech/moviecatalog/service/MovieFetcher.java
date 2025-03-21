package com.precisiontech.moviecatalog.service ;

import com.precisiontech.moviecatalog.model.Movie;
import java.util.List;

public interface MovieFetcher {
    List<Movie> fetchMovies();
}
