package com.springboot.starling.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class Transaction {

    @JsonProperty("feedItemUid")
    @NonNull
    private String feedItemUid;

    @JsonProperty("categoryUid")
    @NonNull
    private String categoryUid;

    @JsonProperty("amount")
    @NonNull
    private Amount amount;

    @JsonProperty("sourceAmount")
    private Amount sourceAmount;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("transactionTime")
    private LocalDateTime transactionTime;

    @JsonProperty("settlementTime")
    private LocalDateTime settlementTime;

    @JsonProperty("source")
    private String source;
}
