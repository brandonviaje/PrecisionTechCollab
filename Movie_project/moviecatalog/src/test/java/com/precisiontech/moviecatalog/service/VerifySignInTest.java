package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VerifySignInTest {

    private static MockWebServer mockWebServer;
    private static VerifySignIn verifySignIn;
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

        verifySignIn = new VerifySignIn(WebClient.builder(), supabaseConfig);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testVerifySignInSuccess() {
        // Mock response with a valid account
        String responseBody = "[ {\"username\": \"testuser\", \"password\": \"password123\"} ]";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        boolean result = verifySignIn.verifySignIn("testuser", "password123");
        assertTrue(result, "User should be able to sign in successfully");
        System.out.println("Test Passed: User successfully signed in.");
    }

    @Test
    void testVerifySignInFailure() {
        // Mock response with no matching accounts
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[]")
                .addHeader("Content-Type", "application/json"));

        boolean result = verifySignIn.verifySignIn("invaliduser", "wrongpassword");
        assertFalse(result, "User should not be able to sign in with incorrect credentials");
        System.out.println("Test Passed: Invalid credentials, sign-in failed as expected.");
    }
}
