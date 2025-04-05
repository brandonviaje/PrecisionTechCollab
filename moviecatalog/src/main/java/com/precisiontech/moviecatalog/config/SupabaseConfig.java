package com.precisiontech.moviecatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    public String getSupabaseUrl() {return supabaseUrl;}

    public String getSupabaseApiKey() {return supabaseApiKey;}
}
