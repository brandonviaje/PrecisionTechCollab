package com.precisiontech.moviecatalog.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WebClientConfigTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void testWebClientBean() {
        assertNotNull(webClient, "WebClient should be created and injected");
    }
}
