package com.springboot.starling.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
@Data
public class Amount {

    @JsonProperty("currency")
    @NonNull
    private String currency;

    @JsonProperty("minorUnits")
    @NonNull
    private int minorUnits;
}
