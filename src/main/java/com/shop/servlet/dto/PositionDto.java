package com.shop.servlet.dto;

import com.shop.model.BeerInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PositionDto {

    private Integer id;
    private String name;
    private String beerType;
    private Double alcoholPercentage;
    private Integer bitterness;
    private String containerType;
    private BeerInfo beerInfo;
}
