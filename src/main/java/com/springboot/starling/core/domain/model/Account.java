package com.springboot.starling.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Account {

    @JsonProperty("accountUid")
    @NonNull
    private String accountUid;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("defaultCategory")
     @NonNull
    private String defaultCategory;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("name")
    private String name;
 
}
