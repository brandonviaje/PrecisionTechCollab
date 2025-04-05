package com.precisiontech.moviecatalog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void setUp() {
        // Initialize the Account object before each test
        account = new Account("John Doe", "john_doe", "password123");
    }

    @Test
    public void testConstructorAndGetters() {
        // Testing constructor and getters
        assertEquals("John Doe", account.getFullName());
        assertEquals("john_doe", account.getUsername());
        assertEquals("password123", account.getPassword());
        assertNull(account.getAccountId()); // accountId is null by default
    }

    @Test
    public void testSetters() {
        // Testing setters
        account.setFullName("Jane Doe");
        account.setUsername("jane_doe");
        account.setPassword("newpassword123");
        account.setAccountId("12345");

        // Verifying the updated values using getters
        assertEquals("Jane Doe", account.getFullName());
        assertEquals("jane_doe", account.getUsername());
        assertEquals("newpassword123", account.getPassword());
        assertEquals("12345", account.getAccountId());
    }

    @Test
    public void testAccountIdSetter() {
        // Testing only the setter for accountId
        account.setAccountId("abc123");
        assertEquals("abc123", account.getAccountId());
    }
}
