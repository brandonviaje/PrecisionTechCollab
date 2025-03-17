package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.model.Account;
import com.precisiontech.moviecatalog.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    // Use Service Role Key (since RLS is disabled)
    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private WebClient webClient;
    private List<Movie> accounts = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(supabaseUrl).build();
    }

    public void addAccount(Account account) {
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("full_name", account.getUsername());
        accountData.put("username", account.getUsername());
        accountData.put("password", account.getPassword());

        try {
            // Serialize the data into JSON format
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(accountData);

            // Insert account into Supabase
            String response = webClient.post()
                    .uri("/rest/v1/accounts")
                    .header("apikey", supabaseApiKey)
                    .header("Prefer", "return=representation") // Ensures the response returns the inserted record
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse the response to get the new account_id
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseNode = responseMapper.readTree(response);
            String accountId = responseNode.get(0).get("account_id").asText();

            System.out.println("Account added with ID: " + accountId);
            account.setAccountId(accountId);
        } catch (Exception e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
    }

    public boolean verifySignIn(String username, String password) {
        List<Account> allAccounts = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/accounts")
                        .queryParam("order", "id.asc")
                        .build())
                .header("apikey", supabaseApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Account.class)
                .collectList()
                .block();

        for (Account account : allAccounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
