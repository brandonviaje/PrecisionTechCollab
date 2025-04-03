package com.precisiontech.moviecatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

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


    /**
     * Creates and configures a {@link WebClient} bean for making HTTP requests to Supabase.
     *
     * @return a configured instance of {@link WebClient}
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("apikey", supabaseApiKey) // Add the API key in the default headers
                .build();
    }
}
