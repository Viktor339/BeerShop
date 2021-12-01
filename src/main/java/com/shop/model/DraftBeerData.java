package com.shop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DraftBeerData extends BeerInfo {
    private String availableLiters;

    public DraftBeerData(String availableLiters){
        this.availableLiters=availableLiters;
    }
}
