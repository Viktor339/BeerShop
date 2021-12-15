package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DraftBuyBeerQuantity implements BuyBeerQuantity {
    private Double litres;
}
