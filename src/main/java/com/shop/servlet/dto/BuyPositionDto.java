package com.shop.servlet.dto;

import com.shop.model.BuyBeerQuantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BuyPositionDto {
    private PositionDto positionDto;
    private Integer userId;
    private BuyBeerQuantity quantity;
    private String quantityType;
}
