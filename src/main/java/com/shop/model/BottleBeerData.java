package com.shop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BottleBeerData extends BeerInfo {
    private String containerVolume;
    private String quantity;

    public BottleBeerData(String containerVolume,String quantity){
        this.containerVolume=containerVolume;
        this.quantity=quantity;
    }
}
