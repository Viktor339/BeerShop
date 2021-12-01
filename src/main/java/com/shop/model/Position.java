package com.shop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Position {
    private long id;
    private String name;
    private String containerType;
    private String containerVolume;
    private String quantity;
    private String beerType;
    private int alcoholPercentage;
    private String bitterness;
    private String availableLitres;
}
