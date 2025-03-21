package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class VerifySignIn {

    private final WebClient webClient;
    private final SupabaseConfig supabaseConfig;

    @Autowired
    public VerifySignIn(WebClient webClient, SupabaseConfig supabaseConfig) {
        this.webClient = webClient;
        this.supabaseConfig = supabaseConfig;
    }

    /**
     * Checks whether the user's account exists when signing in.
     *
     * @param username          the user's username
     * @param password          the user's password
     * @return                  a flag indicating whether their account exists
     */
    public boolean verifySignIn(String username, String password) {
        List<Account> allAccounts = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/v1/accounts")
                        .queryParam("order", "id.asc")
                        .build())
                .header("apikey", supabaseConfig.getSupabaseApiKey())
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
