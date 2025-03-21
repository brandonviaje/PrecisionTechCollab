package com.precisiontech.moviecatalog.service;

import com.precisiontech.moviecatalog.config.SupabaseConfig;
import com.precisiontech.moviecatalog.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddAccountTest {

    @Mock
    private WebClient webClient;

    @Mock
    private SupabaseConfig supabaseConfig;

    @InjectMocks
    private AddAccount addAccountService;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddAccount() throws Exception {
        Account account = new Account("John Doe", "johndoe", "password123");

        // Mock Supabase API key
        when(supabaseConfig.getSupabaseApiKey()).thenReturn("test-api-key");

        // Mock WebClient behavior
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/rest/v1/accounts")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq("apikey"), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq("Prefer"), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq("Content-Type"), anyString())).thenReturn(requestBodyUriSpec);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestBodyUriSpec.bodyValue(any(String.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just("[{\"account_id\":\"12345\"}]"));

        // Call the method
        addAccountService.addAccount(account);

        // Verify account ID is set correctly
        assert account.getAccountId().equals("12345");

        // Verify WebClient interactions
        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri("/rest/v1/accounts");
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }
}
