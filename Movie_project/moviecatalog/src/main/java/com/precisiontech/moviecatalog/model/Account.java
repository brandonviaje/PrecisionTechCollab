package com.precisiontech.moviecatalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class Account {
    private String fullName;
    private String username;
    private String password;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("join_date")
    private String joinDate;

    public Account(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.joinDate = LocalDate.now().toString(); // Set join date to current date by default
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}