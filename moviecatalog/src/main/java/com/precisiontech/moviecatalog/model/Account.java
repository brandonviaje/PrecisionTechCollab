package com.precisiontech.moviecatalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    private String fullName;
    private String username;
    private String password;
    @JsonProperty("account_id")
    private String accountId;

    // Default constructor for Jackson deserialization
    public Account() {
        // Required for proper JSON deserialization
    }

    public Account(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }


    // ------------- Getters and Setters -------------


    /**
     * Gets the user's full name.
     *
     * @return the full name as a String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the user's full name.
     *
     * @param fullName      the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the user's username.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     *
     * @param username      the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's password.
     *
     * @return the password as a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password      the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's account id.
     *
     * @return the id as a String
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the user's account id.
     *
     * @param accountId      the new account id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    // ------------- Helper Functions -------------


    /**
     * Gets a string representation of the {@code Account} object
     *
     * @return a string containing the account details
     */
    @Override
    public String toString() {
        return "Account{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}