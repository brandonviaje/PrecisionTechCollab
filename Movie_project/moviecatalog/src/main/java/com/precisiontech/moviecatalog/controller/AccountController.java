package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Account;
import com.precisiontech.moviecatalog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Spring Boot controller method to handle HTTP POST requests sent from the front end to the "/accounts" endpoint
     * Handles the creation of an account by the user
     *
     * @param fullName          the user's full name
     * @param username          the user's account username
     * @param password          the user's account password
     * @return                  message indicating the account was added
     */
    @PostMapping("/accounts")
    public ResponseEntity<?> addAccount(
            @RequestParam("fullName") String fullName,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        Account account = new Account(fullName, username, password);
        accountService.addAccount(account);

        return ResponseEntity.ok("Account added successfully");
    }

    /**
     * Spring Boot controller method to handle HTTP GET requests sent from the front end to the "/accounts" endpoint
     * Handles the login by a user
     *
     * @param username          the user's account username
     * @param password          the user's account password
     * @return                  a flag indicating whether the account exists
     */
    @GetMapping("/accounts")
    public ResponseEntity<Boolean> handleSignIn(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        boolean accountFound = accountService.verifySignIn(username, password);
        return ResponseEntity.ok(accountFound);
    }
}
