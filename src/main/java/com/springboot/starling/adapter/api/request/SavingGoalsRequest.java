package com.springboot.starling.adapter.api.request;

import com.springboot.starling.adapter.api.dto.AmountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingGoalsRequest {
    private String name;
    private String currency;
    private AmountDTO target;
    private String base64EncodedPhoto;
}
