package com.precisiontech.moviecatalog.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "supabase.url=http://localhost:5432",
        "supabase.api.key=test-api-key"
})
class SupabaseConfigTest {

    @Autowired
    private SupabaseConfig supabaseConfig;

    @Test
    void testGetSupabaseUrl() {
        assertEquals("http://localhost:5432", supabaseConfig.getSupabaseUrl());
    }

    @Test
    void testGetSupabaseApiKey() {
        assertEquals("test-api-key", supabaseConfig.getSupabaseApiKey());
    }
}
