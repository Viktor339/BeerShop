package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BottleBeerData implements BeerInfo {
    private Double containerVolume;
    private Double quantity;
}
