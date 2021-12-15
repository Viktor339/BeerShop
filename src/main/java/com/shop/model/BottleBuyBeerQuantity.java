package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BottleBuyBeerQuantity implements BuyBeerQuantity {
    private Integer bottles;
}
