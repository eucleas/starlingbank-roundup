package com.springboot.starling.adapter.services;

import com.springboot.starling.adapter.api.dto.AccountTransactionsDTO;
import com.springboot.starling.adapter.api.dto.AmountDTO;
import com.springboot.starling.adapter.api.request.SavingGoalsRequest;
import com.springboot.starling.adapter.api.response.GetAccountsResponse;
import com.springboot.starling.adapter.api.response.GetTransactionsResponse;
import com.springboot.starling.adapter.api.response.SavingGoalsResponse;
import com.springboot.starling.core.domain.model.Account;
import com.springboot.starling.core.domain.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 * Service: SavingGoalsService
 * 
 * Description:
 * This service handles the creation and processing of saving goals for a user's account
 * Request Items:
 * minTimestamp (String): The minimum timestamp for filtering transactions.
 * authorizationHeader (String): The authorization header containing the token for authentication 
 * 
 * Example:
 * SavingGoalsService service = new SavingGoalsService();
 * service.processSavingGoals("2024-07-21T00:00:00.000Z", "Bearer token");
 */

@Service
public class SavingGoalsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavingGoalsService.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final int WEEKLY_TIME_INTERVAL = 7;
    private static final int ROUND_UP_CONSTANT = 100;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Value("${starling.create-savinggoals}")
    public String apiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public SavingGoalsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SavingGoalsResponse processSavingGoals(String minTimestamp, String authorizationHeader) {
        SavingGoalsResponse response = null;
        try {
            Account account = getAccount(authorizationHeader);

            if (account != null) {
                String accountUid = account.getAccountUid();

                List<Transaction> transactions = getTransactions(account, minTimestamp, authorizationHeader);
                if (transactions != null && !transactions.isEmpty()) {
                    int totalSavingMinorUnits = calculateTotalSavingMinorUnits(transactions);
                    String currency = transactions.get(0).getAmount().getCurrency();
                    SavingGoalsRequest savingGoalsRequest = new SavingGoalsRequest();
                    savingGoalsRequest.setName("date of " + minTimestamp.substring(0, 10) + " saving");
                    savingGoalsRequest.setCurrency(currency);
                    savingGoalsRequest.setTarget(new AmountDTO(currency, totalSavingMinorUnits));

                    response = createSavingGoal(savingGoalsRequest, accountUid, authorizationHeader);
                } else {
                    LOGGER.warn("No transactions found.");
                }
            } else {
                LOGGER.warn("No accounts found.");
            }
        } catch (Exception e) {
            LOGGER.error("Error processing saving goals: ", e);
        }

        if (response == null) {
            LOGGER.warn("SavingGoalsResponse is null.");
        }
        return response;
    }

    private Account getAccount(String authorizationHeader) throws IOException {
        GetAccountsResponse accountResponse = accountService.getAccounts(authorizationHeader);
        if (accountResponse.getAccounts() != null && !accountResponse.getAccounts().isEmpty()) {
            return accountResponse.getAccounts().get(0);
        } else {
            LOGGER.warn("No accounts available.");
        }
        return null;
    }

    private List<Transaction> getTransactions(Account account, String minTimestamp, String authorizationHeader) {
        try {
            String accountUid = account.getAccountUid();
            String defaultCategory = account.getDefaultCategory();

            LocalDateTime minTime = LocalDateTime.parse(minTimestamp, TIMESTAMP_FORMATTER);
            LocalDateTime maxTime = minTime.plus(WEEKLY_TIME_INTERVAL, ChronoUnit.DAYS);
            String maxTimestamp = maxTime.format(TIMESTAMP_FORMATTER);

            AccountTransactionsDTO dto = new AccountTransactionsDTO(accountUid, defaultCategory, minTimestamp, maxTimestamp);
            GetTransactionsResponse transactionsResponse = transactionService.getTransactions(dto, authorizationHeader);

            return transactionsResponse.getFeedItems();
        } catch (Exception e) {
            LOGGER.error("Error getting transactions: ", e);
            return Collections.emptyList();
        }
    }

    public SavingGoalsResponse createSavingGoal(SavingGoalsRequest savingGoalsRequest, String accountUid, String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("Accept", "application/json");
            headers.set("Content-Type", "application/json");

            HttpEntity<SavingGoalsRequest> entity = new HttpEntity<>(savingGoalsRequest, headers);
            String url = apiUrl + accountUid + "/savings-goals";

            ResponseEntity<SavingGoalsResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    SavingGoalsResponse.class);

            LOGGER.info("Saving goal created successfully: " + response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Error creating saving goal: ", e);
            return new SavingGoalsResponse(null, false);
        }
    }

    public static int calculateTotalSavingMinorUnits(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return 0;
        }
//last version added here with mod
        return transactions.stream()
                .filter(item -> "OUT".equals(item.getDirection()))
                .filter(item-> item.getAmount().getMinorUnits() % ROUND_UP_CONSTANT!=0)
                .mapToInt(item -> ROUND_UP_CONSTANT - (item.getAmount().getMinorUnits() % ROUND_UP_CONSTANT))
                .sum();
    }
}
