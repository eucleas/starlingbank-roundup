package com.springboot.starling;

import com.springboot.starling.adapter.api.dto.AccountTransactionsDTO;
import com.springboot.starling.adapter.api.dto.AmountDTO;
import com.springboot.starling.adapter.api.request.SavingGoalsRequest;
import com.springboot.starling.adapter.api.response.GetAccountsResponse;
import com.springboot.starling.adapter.api.response.GetTransactionsResponse;
import com.springboot.starling.adapter.api.response.SavingGoalsResponse;
import com.springboot.starling.adapter.controller.AccountController;
import com.springboot.starling.adapter.services.SavingGoalsService;
import com.springboot.starling.adapter.services.TransactionService;
import com.springboot.starling.core.domain.model.Account;
import com.springboot.starling.core.domain.model.Amount;
import com.springboot.starling.core.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 * Service: SavingGoalsServiceTest
 * 
 * Description: Testing of SavingGoalsServiceTest class and relevant objects
 */
@SpringBootTest
public class SavingGoalsServiceTest {

    @Mock
    private AccountController accountController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SavingGoalsService savingGoalsService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        savingGoalsService = new SavingGoalsService(restTemplate);
    }

    @Test
     void testCalculateTotalSavingMinorUnits() {
        Amount amount1 = new Amount();
        amount1.setCurrency("GBP");
        amount1.setMinorUnits(800);
    
        Transaction transaction1 = new Transaction("deeb87be-bf6e-4ecd-965f-286e1bb66d54", "deebbb63-cfad-446a-99a6-3ae0950999ca", amount1);
        transaction1.setDirection("OUT");
    
        Amount amount2 = new Amount();
        amount2.setCurrency("GBP");
        amount2.setMinorUnits(500);
    
        Transaction transaction2 = new Transaction("deeb87be-bf6e-4ecd-965f-286e1bb66d54", "deebbb63-cfad-446a-99a6-3ae0950999ca", amount2);
        transaction2.setDirection("OUT");
    
        Amount amount3 = new Amount();
        amount3.setCurrency("GBP");
        amount3.setMinorUnits(400);
    
        Transaction transaction3 = new Transaction("deeb87be-bf6e-4ecd-965f-286e1bb66d54", "deebbb63-cfad-446a-99a6-3ae0950999ca", amount3);
        transaction3.setDirection("OUT");
    
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);
    
        // Calculate total saving minor units
        int totalSavingMinorUnits = SavingGoalsService.calculateTotalSavingMinorUnits(transactions);
    
        // Expected value: 
        // ROUND_UP_CONSTANT - (87 % 100) = 100 - 87 = 13
        // ROUND_UP_CONSTANT - (520 % 100) = 100 - 20 = 80
        // ROUND_UP_CONSTANT - (435 % 100) = 100 - 35 = 65
        // Total = 13 + 80 + 65 = 158
        assertEquals(0, totalSavingMinorUnits, "Total saving minor units should be 158");
    }
    

    @Test
     void testProcessSavingGoals_NoTransactions() throws IOException {
        String minTimestamp = "2024-07-21T00:00:00.000Z";
        String authorizationHeader = "Bearer test-token";

        Account account = new Account();
        account.setAccountUid("deeb87be-bf6e-4ecd-965f-286e1bb66d54");
        account.setDefaultCategory("default-category");

        GetAccountsResponse accountResponse = new GetAccountsResponse();
        accountResponse.setAccounts(Collections.singletonList(account));

        GetTransactionsResponse transactionsResponse = new GetTransactionsResponse();
        transactionsResponse.setFeedItems(Collections.emptyList());

        // Mock behaviors
        when(accountController.getAccounts(anyString())).thenReturn(accountResponse);
        when(transactionService.getTransactions(any(AccountTransactionsDTO.class), anyString())).thenReturn(transactionsResponse);

        // Call the method and assert the response
        SavingGoalsResponse result = savingGoalsService.processSavingGoals(minTimestamp, authorizationHeader);
        assertEquals(null, result);
    }

    @Test
     void testProcessSavingGoals_NoAccount() throws IOException {
        // Prepare test data
        String minTimestamp = "2024-07-21T00:00:00.000Z";
        String authorizationHeader = "Bearer test-token";

        GetAccountsResponse accountResponse = new GetAccountsResponse();
        accountResponse.setAccounts(Collections.emptyList());

        // Mock behaviors
       when(accountController.getAccounts(anyString())).thenReturn(accountResponse);

        // Call the method and assert the response
        SavingGoalsResponse result = savingGoalsService.processSavingGoals(minTimestamp, authorizationHeader);
        assertEquals(null, result);
    }

    @Test
     void testCreateSavingGoal() {
        // Prepare test data
        SavingGoalsRequest savingGoalsRequest = new SavingGoalsRequest(
                "Test Goal",
                "GBP",
                new AmountDTO("GBP", 123456),
                "string"
        );
        String accountUid = "deeb87be-bf6e-4ecd-965f-286e1bb66d54";
        String authorizationHeader = "Bearer test-token";

        SavingGoalsResponse expectedResponse = new SavingGoalsResponse("feff5cc0-7be3-4d67-bf2c-fb3dda82abd3", true);

        // Mock behaviors
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(SavingGoalsResponse.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        // Call the method and assert the response
        SavingGoalsResponse result = savingGoalsService.createSavingGoal(savingGoalsRequest, accountUid, authorizationHeader);
        assertNotNull(result);
        assertEquals("feff5cc0-7be3-4d67-bf2c-fb3dda82abd3", result.getSavingsGoalUid());
        assertEquals(true, result.isSuccess());
    }
}
