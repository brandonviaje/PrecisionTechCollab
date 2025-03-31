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
    public VerifySignIn(WebClient.Builder webClientBuilder, SupabaseConfig supabaseConfig) {
        this.webClient = webClientBuilder.baseUrl(supabaseConfig.getSupabaseUrl()).build();
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
        System.out.println("Making API call to Supabase for verifySignIn");
        try {
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

            System.out.println("Retrieved " + (allAccounts != null ? allAccounts.size() : 0) + " accounts");

            for (Account account : allAccounts) {
                if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in verifySignIn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Finds and verifies an account by username and password.
     * Used by the controller to return the full account details.
     *
     * @param username          the user's username
     * @param password          the user's password
     * @return                  the Account if found and verified, null otherwise
     */
    public Account findAndVerifyAccount(String username, String password) {
        System.out.println("Making API call to Supabase for findAndVerifyAccount");
        try {
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

            System.out.println("Retrieved " + (allAccounts != null ? allAccounts.size() : 0) + " accounts");

            if (allAccounts != null) {
                for (Account account : allAccounts) {
                    if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                        return account;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error in findAndVerifyAccount: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}