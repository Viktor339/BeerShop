package com.shop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Position {
    private Integer id;
    private String name;
    private String beerType;
    private Double alcoholPercentage;
    private Integer bitterness;
    private String containerType;
    private BeerInfo beerInfo;
}
