package com.precisiontech.moviecatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    /**
     * The URL of the Supabase database, injected from the application properties.
     */
    @Value("${supabase.url}")
    private String supabaseUrl;

    /**
     * The API key for authenticating with Supabase, injected from the application properties.
     */
    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    // ------------- Getters and Setters -------------


    /**
     * Gets the Supabase URL.
     *
     * @return the Supabase URL as a String
     */
    public String getSupabaseUrl() {return supabaseUrl;}

    /**
     * Gets the Supabase API key.
     *
     * @return the Supabase API key as a String
     */
    public String getSupabaseApiKey() {return supabaseApiKey;}
}
