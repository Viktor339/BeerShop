package com.shop.servlet.dto;

import com.shop.model.BeerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ChangePositionResponse {
    private long id;
    private String name;
    private String containerType;
    private BeerInfo beerInfo;
}
