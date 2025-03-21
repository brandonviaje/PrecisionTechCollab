package com.precisiontech.moviecatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddAccount {

    private final WebClient webClient;
    private final SupabaseConfig supabaseConfig;

    @Autowired
    public AddAccount(WebClient webClient, SupabaseConfig supabaseConfig) {
        this.webClient = webClient;
        this.supabaseConfig = supabaseConfig;
    }

    /**
     * Adds an account to the account's database.
     *
     * @param account the account to be added
     */
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
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
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
}
