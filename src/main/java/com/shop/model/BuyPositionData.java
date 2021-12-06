package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuyPositionData {
    private List<BuyBottleBeerData> buyBottleBeerData;
    private List<BuyDraftBeerData> buyDraftBeerData;
}
