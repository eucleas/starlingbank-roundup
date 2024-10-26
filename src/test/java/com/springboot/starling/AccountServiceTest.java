package com.springboot.starling;

import com.springboot.starling.adapter.api.response.GetAccountsResponse;
import com.springboot.starling.adapter.services.AccountService;
import com.springboot.starling.core.domain.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 * Service: AccountServiceTest
 * 
 * Description: Testing of AccountService class and relevant objects
 */

@SpringBootTest
class AccountServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountService accountService;

    private String apiUrl = "https://testurl";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService(restTemplate);
        accountService.apiUrl = apiUrl; // Set the apiUrl field directly
    }

    @Test
    void testGetAccounts_Success() {
        // Mock response
        GetAccountsResponse mockResponse = new GetAccountsResponse();

        Account account = new Account();
        account.setAccountUid("f8715c09-9022-46a1-9b02-ee19931da20e");
        account.setAccountType("PRIMARY");
        account.setDefaultCategory("f8713b96-00c7-44c1-a13e-b41307aa20aa");
        account.setCurrency("GBP");
        account.setCreatedAt(LocalDateTime.parse("2024-07-28T12:33:13.275Z", DateTimeFormatter.ISO_DATE_TIME));
        account.setName("Personal");

        mockResponse.setAccounts(Collections.singletonList(account));
        //arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer token");
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                GetAccountsResponse.class))
                .thenReturn(ResponseEntity.ok(mockResponse));
        //act
        GetAccountsResponse response = accountService.getAccounts("Bearer token");
        //asserts
        verify(restTemplate, times(1)).exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                GetAccountsResponse.class);

        assertNotNull(response);
        assertEquals(1, response.getAccounts().size());

        Account returnedAccount = response.getAccounts().get(0);
        assertEquals("f8715c09-9022-46a1-9b02-ee19931da20e", returnedAccount.getAccountUid());
        assertEquals("PRIMARY", returnedAccount.getAccountType());
        assertEquals("f8713b96-00c7-44c1-a13e-b41307aa20aa", returnedAccount.getDefaultCategory());
        assertEquals("GBP", returnedAccount.getCurrency());
        assertEquals(LocalDateTime.parse("2024-07-28T12:33:13.275Z", DateTimeFormatter.ISO_DATE_TIME), returnedAccount.getCreatedAt());
        assertEquals("Personal", returnedAccount.getName());
    }

    @Test
    void testGetAccounts_Error() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer token");
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                GetAccountsResponse.class))
                .thenThrow(new RuntimeException("API error"));

        GetAccountsResponse response = accountService.getAccounts("Bearer token");

        verify(restTemplate, times(1)).exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                GetAccountsResponse.class);

        assertNotNull(response);
        assertTrue(response.getAccounts().isEmpty()); // empty list control
    }
}
