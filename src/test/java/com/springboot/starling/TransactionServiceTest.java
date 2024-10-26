package com.springboot.starling;

import com.springboot.starling.adapter.api.dto.AccountTransactionsDTO;
import com.springboot.starling.adapter.api.response.GetTransactionsResponse;
import com.springboot.starling.adapter.services.TransactionService;
import com.springboot.starling.core.domain.model.Amount;
import com.springboot.starling.core.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 * Service: TransactionServiceTest
 * 
 * Description: Testing of TransactionService class and relevant dto objects
 */

@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransactionService transactionService;

    @Value("${starling.transaction-url}")
    private String apiUrl = "https://api-sandbox.starlingbank.com/api";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(restTemplate);
    }

    @Test
    public void testGetTransactions_Success() {
        // Prepare the DTO and authorization header
        AccountTransactionsDTO dto = new AccountTransactionsDTO(
            "f8715c09-9022-46a1-9b02-ee19931da20e",
            "f8713b96-00c7-44c1-a13e-b41307aa20aa",
            "2024-07-23T12:34:56.000Z",
            "2024-07-29T12:34:56.000Z"
        );
        String authorizationHeader = "Bearer test-token";

        // Prepare the response object with mock data
        Amount amount1 = new Amount();
        amount1.setCurrency("GBP");
        amount1.setMinorUnits(87);
        GetTransactionsResponse response = new GetTransactionsResponse();
        Transaction transaction1 = new Transaction(authorizationHeader, authorizationHeader, amount1);
        transaction1.setFeedItemUid("f8747d53-6e55-4d03-920b-5a1a9a30aabb");
        transaction1.setCategoryUid("f8713b96-00c7-44c1-a13e-b41307aa20aa");
        transaction1.setAmount(amount1);
        transaction1.setDirection("OUT");
        transaction1.setUpdatedAt(LocalDateTime.parse("2024-07-28T12:36:32.414"));
        transaction1.setTransactionTime(LocalDateTime.parse("2024-07-28T12:36:31.281"));
        transaction1.setSettlementTime(LocalDateTime.parse("2024-07-28T12:36:32.377"));
        transaction1.setSource("FASTER_PAYMENTS_OUT");

        Amount amount2 = new Amount();
        amount2.setCurrency("GBP");
        amount2.setMinorUnits(520);
        Transaction transaction2 = new Transaction(authorizationHeader, authorizationHeader, amount1);
        transaction2.setFeedItemUid("f8748bdd-12cb-4c2c-af30-50dfaef24446");
        transaction2.setCategoryUid("f8713b96-00c7-44c1-a13e-b41307aa20aa");
        transaction2.setAmount(amount2);
        transaction2.setDirection("OUT");
        transaction2.setUpdatedAt(LocalDateTime.parse("2024-07-28T12:36:13.551"));
        transaction2.setTransactionTime(LocalDateTime.parse("2024-07-28T12:36:12.634"));
        transaction2.setSettlementTime(LocalDateTime.parse("2024-07-28T12:36:13.485"));
        transaction2.setSource("FASTER_PAYMENTS_OUT");

        response.setFeedItems(Arrays.asList(transaction1, transaction2));

        // Mock the RestTemplate behavior
        when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
        )).thenReturn(ResponseEntity.ok(response));

        // Call the method and assert the response
        GetTransactionsResponse result = transactionService.getTransactions(dto, authorizationHeader);
        assertNotNull(result);
        assertEquals(2, result.getFeedItems().size());

        // Assert the details of the first transaction
        Transaction resultTransaction1 = result.getFeedItems().get(0);
        assertEquals("f8747d53-6e55-4d03-920b-5a1a9a30aabb", resultTransaction1.getFeedItemUid());
        assertEquals("f8713b96-00c7-44c1-a13e-b41307aa20aa", resultTransaction1.getCategoryUid());
        assertEquals("GBP", resultTransaction1.getAmount().getCurrency());
        assertEquals(87, resultTransaction1.getAmount().getMinorUnits());
        assertEquals("OUT", resultTransaction1.getDirection());
        assertEquals(LocalDateTime.parse("2024-07-28T12:36:32.414"), resultTransaction1.getUpdatedAt());

        // Assert the details of the second transaction
        Transaction resultTransaction2 = result.getFeedItems().get(1);
        assertEquals("f8748bdd-12cb-4c2c-af30-50dfaef24446", resultTransaction2.getFeedItemUid());
        assertEquals("f8713b96-00c7-44c1-a13e-b41307aa20aa", resultTransaction2.getCategoryUid());
        assertEquals("GBP", resultTransaction2.getAmount().getCurrency());
        assertEquals(520, resultTransaction2.getAmount().getMinorUnits());
        assertEquals("OUT", resultTransaction2.getDirection());
        assertEquals(LocalDateTime.parse("2024-07-28T12:36:13.551"), resultTransaction2.getUpdatedAt());
    }

    @Test
    public void testGetTransactions_Error() {
        // Prepare the DTO and authorization header
        AccountTransactionsDTO dto = new AccountTransactionsDTO(
            "f8715c09-9022-46a1-9b02-ee19931da20e",
            "f8713b96-00c7-44c1-a13e-b41307aa20aa",
            "2024-07-23T12:34:56.000Z",
            "2024-07-29T12:34:56.000Z"
        );
        String authorizationHeader = "Bearer test-token";

        // Mock the RestTemplate behavior to throw an exception
        when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
        )).thenThrow(new RuntimeException("Error occurred"));

        // Call the method and assert the response
        GetTransactionsResponse result = transactionService.getTransactions(dto, authorizationHeader);
        assertNotNull(result);
        assertEquals(0, result.getFeedItems().size());
    }
}
