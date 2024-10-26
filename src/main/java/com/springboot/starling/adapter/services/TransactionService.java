package com.springboot.starling.adapter.services;

import com.springboot.starling.adapter.api.dto.AccountTransactionsDTO;
import com.springboot.starling.adapter.api.response.GetTransactionsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 * Service: TransactionService
 * 
 * Description:
 * returns transaction feedid details belongs to an Account
 * Request Items:
 * AccountTransactionDTO items
 */
@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Value("${starling.transaction-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public TransactionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GetTransactionsResponse getTransactions(AccountTransactionsDTO dto, String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.set("Accept", "application/json");

        String url = String.format("%s/v2/feed/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s", 
            apiUrl, dto.getAccountUid(), dto.getDefaultCategory(), dto.getMinTimestamp(), dto.getMaxTimestamp());

        LOGGER.debug("Constructed URL: {}", url);
        LOGGER.debug("Request Headers: {}", headers);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GetTransactionsResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    GetTransactionsResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Error retrieving transactions: ", e);
            return new GetTransactionsResponse(); // Return an empty response in case of error
        }
    }
}
