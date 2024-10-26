package com.springboot.starling.adapter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 * Data Transfer object of Account&Transaction for request headers, parameters
 */
public class AccountTransactionsDTO {

    private String accountUid;
    private String defaultCategory;
    private String minTimestamp;
    private String maxTimestamp;
}
