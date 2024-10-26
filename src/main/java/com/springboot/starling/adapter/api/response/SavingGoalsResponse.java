package com.springboot.starling.adapter.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingGoalsResponse {
    private String savingsGoalUid;
    private boolean success;

}
