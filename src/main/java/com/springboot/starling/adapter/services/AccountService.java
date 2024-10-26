package com.springboot.starling.adapter.services;

import com.springboot.starling.adapter.api.response.GetAccountsResponse;
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
 * Service: AccountService
 * 
 * Description: returns account id details.
 * Request Items:
 * authorizationHeader (String): The authorization header containing the token for authentication.
 */

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Value("${starling.account-url}")
    public String apiUrl;

    private final RestTemplate restTemplate;

    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GetAccountsResponse getAccounts(String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<GetAccountsResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    GetAccountsResponse.class);

            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Error retrieving accounts: ", e);
            return new GetAccountsResponse(); // Return an initialized response in case of error
        }
    }
}
