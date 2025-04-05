package com.precisiontech.moviecatalog.controller;

import com.precisiontech.moviecatalog.model.Account;
import com.precisiontech.moviecatalog.service.AddAccount;
import com.precisiontech.moviecatalog.service.VerifySignIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final VerifySignIn verifySignInService;
    private final AddAccount addAccountService;

    // Constructor injection
    @Autowired
    public AccountController(VerifySignIn verifySignInService, AddAccount addAccountService) {
        this.verifySignInService = verifySignInService;
        this.addAccountService = addAccountService;
    }

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

        System.out.println("Received account creation request for username: " + username);

        // Basic validation
        if (fullName == null || fullName.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        try {
            Account account = new Account(fullName, username, password);
            addAccountService.addAccount(account);
            System.out.println("Account created successfully for: " + username);

            return ResponseEntity.ok("Account added successfully");
        } catch (Exception e) {
            System.err.println("Error creating account: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating account: " + e.getMessage());
        }
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
    public ResponseEntity<?> handleSignIn(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        System.out.println("Sign-in attempt for user: " + username);

        try {
            Account account = verifySignInService.findAndVerifyAccount(username, password);
            System.out.println("Account found: " + (account != null));

            if (account != null) {
                System.out.println("Account details: " + account.getUsername() + ", " + account.getFullName());

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("fullName", account.getFullName());
                response.put("username", account.getUsername());
                return ResponseEntity.ok(response);
            } else {
                System.out.println("No matching account found for: " + username);
                return ResponseEntity.ok(Map.of("success", false, "message", "Invalid username or password"));
            }
        } catch (Exception e) {
            System.err.println("Error during sign-in: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error during sign-in"));
        }
    }
}