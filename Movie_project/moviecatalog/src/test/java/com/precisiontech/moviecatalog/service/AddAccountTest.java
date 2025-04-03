package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Account;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddAccountTest {

    private static MockWebServer mockWebServer;
    private static AddAccount addAccount;
    private static SupabaseConfig supabaseConfig;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        supabaseConfig = mock(SupabaseConfig.class);
        when(supabaseConfig.getSupabaseApiKey()).thenReturn("test-api-key");
        when(supabaseConfig.getSupabaseUrl()).thenReturn(mockWebServer.url("/").toString());

        addAccount = new AddAccount(WebClient.builder(), supabaseConfig);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testAddAccountSuccess() throws Exception {
        Account account = new Account();
        account.setFullName("John Doe");
        account.setUsername("johndoe");
        account.setPassword("password123");

        String mockResponse = "[{\"account_id\": \"12345\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        addAccount.addAccount(account);

        assertEquals("12345", account.getAccountId(), "Account ID should be set after successful creation");
        System.out.println("Test Passed: Account successfully added with ID 12345");
    }

    @Test
    void testAddAccountFailure() {
        Account account = new Account();
        account.setFullName("Jane Doe");
        account.setUsername("janedoe");
        account.setPassword("password123");

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        assertDoesNotThrow(() -> addAccount.addAccount(account), "Should handle server errors gracefully");
        assertNull(account.getAccountId(), "Account ID should not be set when account creation fails");
        System.out.println("Test Passed: Handled server error correctly");
    }
}
