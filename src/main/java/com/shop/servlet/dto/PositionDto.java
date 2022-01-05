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

    // .id(rs.getInt(ID))
    //                        .name(rs.getString(NAME))
    //                        .beerType(rs.getString(BEER_TYPE))
    //                        .alcoholPercentage(rs.getDouble(ALCOHOL_PERCENTAGE))
    //                        .bitterness(rs.getInt(BITTERNESS))
    //                        .containerType(rs.getString(CONTAINER_TYPE))
    //                     //   .beerInfo((BeerInfo) objectMapper.readValue(rs.getString(BEER_INFO), clazz))
    //                        .build();
}
