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

    @PostMapping("/accounts")
    public ResponseEntity<?> addAccount(
            @RequestParam("fullName") String fullName,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        Account account = new Account(fullName, username, password);
        accountService.addAccount(account);

        return ResponseEntity.ok("Account added successfully");
    }

    @GetMapping("/accounts")
    public ResponseEntity<Boolean> handleSignIn(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        boolean accountFound = accountService.verifySignIn(username, password);
        return ResponseEntity.ok(accountFound);
    }
}
