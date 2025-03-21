package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "supabase.url=http://localhost:8080",
        "supabase.api.key=test-api-key"
})
class VerifySignInTest {

    @Autowired
    private SupabaseConfig supabaseConfig;

    private MockWebServer mockWebServer;
    private VerifySignIn verifySignIn;
    private WebClient webClient;

    @BeforeEach
    void setUp() throws Exception {
        // Start MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create the WebClient and inject mockWebServer URL
        webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        verifySignIn = new VerifySignIn(webClient, supabaseConfig);
    }

    @Test
    void testVerifySignIn_validCredentials() throws Exception {
        // Prepare mock response with a list of accounts
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("[{\"username\":\"user1\",\"password\":\"password1\"}," +
                        "{\"username\":\"user2\",\"password\":\"password2\"}]"));

        // Call the method to verify sign-in with valid credentials
        boolean result = verifySignIn.verifySignIn("user1", "password1");

        assertThat(result).isTrue();
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/rest/v1/accounts?order=id.asc");
    }

    @Test
    void testVerifySignIn_invalidCredentials() throws Exception {
        // Prepare mock response with a list of accounts
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("[{\"username\":\"user1\",\"password\":\"password1\"}," +
                        "{\"username\":\"user2\",\"password\":\"password2\"}]"));

        // Call the method to verify sign-in with invalid credentials
        boolean result = verifySignIn.verifySignIn("user3", "password3");

        // Assert that the result is false since credentials are invalid
        assertThat(result).isFalse();
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/rest/v1/accounts?order=id.asc");
    }
}
