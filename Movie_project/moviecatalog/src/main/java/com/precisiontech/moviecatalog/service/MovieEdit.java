package com.precisiontech.moviecatalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class MovieEdit {
    @Value("${supabase.url}")
    private String supabaseUrl;

    // Use Service Role Key (since RLS is disabled)
    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private WebClient webClient;
    private List<Movie> movies = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

     /**
     * Updates a movie's details in the database based on specified conditions.
     *
     * @param title                The title of the movie to update.
     * @param runtime              The new runtime (optional).
     * @param pgRating             The new PG rating (optional).
     * @param synopsis             The new synopsis (optional).
     * @param genres               The new genres (optional).
     * @param productionCompanies  The new production companies (optional).
     * @param spokenLanguages      The new spoken languages (optional).
     * @return 
     */

    public boolean editMovie(
        String title,
        String runtime,
        String pgRating,
        String synopsis,
        String genres,
        String productionCompanies, 
        String spokenLanguages){

        try{
            Map<String, Object> updateData = new HashMap<>();

            //Upate the corresponding data if provided by the user 
            //!null checks if the user actually put somethign and isEmpty makes sures its not an empty string " "
            if (runtime != null && !runtime.isEmpty()) updateData.put("runtime", runtime);
            if (pgRating != null && !pgRating.isEmpty()) updateData.put("pg_rating", pgRating);
            if (synopsis != null && !synopsis.isEmpty()) updateData.put("synopsis", synopsis);
            if (genres != null && !genres.isEmpty()) updateData.put("genres", genres);
            if (productionCompanies != null && !productionCompanies.isEmpty()) updateData.put("productioncompanies", productionCompanies);
            if (spokenLanguages != null && !spokenLanguages.isEmpty()) updateData.put("spoken_languages", spokenLanguages);

            if(updateData.isEmpty()){
                return false;
            }

            String response = webClient.patch() 
                                .uri(uriBuilder -> uriBuilder
                                        .path(supabaseUrl + "/rest/v1/movies")
                                        .queryParam("title", "eq." + title) // Find movie by title
                                        .build())
                                .header("apikey", supabaseApiKey)
                                .header("Prefer", "return=minimal") 
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .bodyValue(updateData)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

            return response == null || response.isEmpty();

        } catch (Exception e){
            System.err.println("Error updating movie: " + e.getMessage());
            return false;
        }
    }
}
