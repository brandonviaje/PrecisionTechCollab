package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Account;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddAccountTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private AddAccount addAccountService;
    private SupabaseConfig supabaseConfig;

    @BeforeEach
    public void setUp() throws Exception {
        // Start MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create WebClient instance pointing to the MockWebServer URL
        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        // Initialize SupabaseConfig and set the API key directly
        supabaseConfig = new SupabaseConfig();
        Field apiKeyField = SupabaseConfig.class.getDeclaredField("supabaseApiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(supabaseConfig, "test-api-key");

        // Initialize AddAccount service
        addAccountService = new AddAccount(webClient, supabaseConfig);

        // Inject WebClient and SupabaseConfig dependencies using reflection
        injectDependenciesUsingReflection(addAccountService, "webClient", webClient);
        injectDependenciesUsingReflection(addAccountService, "supabaseConfig", supabaseConfig);
    }

    private void injectDependenciesUsingReflection(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testAddAccount() throws Exception {
        // Create test account
        Account account = new Account("John Doe", "johndoe", "password123");

        // Mock the HTTP response from MockWebServer
        mockWebServer.enqueue(new MockResponse()
                .setBody("[{\"account_id\":\"12345\"}]")
                .addHeader("Content-Type", "application/json"));

        // Call the method to add account
        addAccountService.addAccount(account);

        // Verify the account ID is correctly set
        assertEquals("12345", account.getAccountId());

        // Verify that the request was made to the expected path
        mockWebServer.takeRequest(); // This ensures that the request was made to the mock server
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Shut down the MockWebServer after tests
        mockWebServer.shutdown();
    }
}
