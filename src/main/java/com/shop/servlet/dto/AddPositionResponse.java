package com.shop.servlet.dto;

import com.shop.model.BeerInfo;
import lombok.Data;

@Data
public class AddPositionResponse {
    private long id;
    private String name;
    private String beerType;
    private int alcoholPercentage;
    private String bitterness;
    private String containerType;

    private BeerInfo beerInfo;

    public AddPositionResponse(long id,String name,String beerType,int alcoholPercentage,String bitterness,String containerType,BeerInfo beerInfo){
        this.id = id;
        this.name = name;
        this.beerType = beerType;
        this.alcoholPercentage = alcoholPercentage;
        this.bitterness = bitterness;
        this.containerType=containerType;
        this.beerInfo =beerInfo;
    }
}
