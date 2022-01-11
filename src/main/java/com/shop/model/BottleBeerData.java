package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class BottleBeerData {
    private Double containerVolume;
    private Integer quantity;
}
