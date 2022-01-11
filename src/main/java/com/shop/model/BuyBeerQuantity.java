package com.shop.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
@Data
public class BuyBeerQuantity {
    private Double litres;
    private Integer bottles;

    public BuyBeerQuantity(Double litres) {
        this.litres = litres;
    }

    public BuyBeerQuantity(Integer bottles) {
        this.bottles = bottles;
    }
}
