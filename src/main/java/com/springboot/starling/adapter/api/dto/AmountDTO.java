package com.springboot.starling.adapter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Ozge Ataseven Ozdol
 * Date: 2024-07-28
 *
 *Transaction feed item's amount data transfer object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountDTO {

    private String currency;
    private int minorUnits;
}
