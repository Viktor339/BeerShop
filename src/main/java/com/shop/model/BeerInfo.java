package com.shop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeerInfo {
    private Double containerVolume;
    private Integer quantity;
    private Double availableLiters;

    public BeerInfo(Double containerVolume, Integer quantity) {
        this.containerVolume = containerVolume;
        this.quantity = quantity;
    }

    public BeerInfo(Double availableLiters) {
        this.availableLiters = availableLiters;
    }
}
