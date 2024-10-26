package com.springboot.starling.adapter.controller;

import com.springboot.starling.adapter.api.dto.AccountTransactionsDTO;
import com.springboot.starling.adapter.api.response.GetTransactionsResponse;
import com.springboot.starling.adapter.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public GetTransactionsResponse getTransactions(
            @RequestParam String accountUid,
            @RequestParam String categoryUid,
            @RequestParam String minTransactionTimestamp,
            @RequestParam String maxTransactionTimestamp,
            @RequestHeader("Authorization") String authorizationHeader) {

        AccountTransactionsDTO dto = new AccountTransactionsDTO(
                accountUid,
                categoryUid,
                minTransactionTimestamp,
                maxTransactionTimestamp
        );

        return transactionService.getTransactions(dto, authorizationHeader);
    }
}
