package com.shop.servlet.dto;

import com.shop.model.BeerInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AddPositionDto {
    private long id;
    private final String name;
    private final String beerType;
    private final Double alcoholPercentage;
    private final Integer bitterness;
    private final String containerType;
    private BeerInfo beerInfo;
}
