package com.springboot.starling.adapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.starling.adapter.api.response.GetAccountsResponse;
import com.springboot.starling.adapter.services.AccountService;

import java.io.IOException;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public GetAccountsResponse getAccounts(@RequestHeader("Authorization") String authorizationHeader) throws IOException {
        return accountService.getAccounts(authorizationHeader);
    }
}
