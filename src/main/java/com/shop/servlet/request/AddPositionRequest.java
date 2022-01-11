package com.shop.servlet.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shop.model.BeerInfo;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddPositionRequest {

    private String name;
    private String beerType;
    private Double alcoholPercentage;
    private Integer bitterness;
    private String containerType;

    private BeerInfo beerInfo;
}
