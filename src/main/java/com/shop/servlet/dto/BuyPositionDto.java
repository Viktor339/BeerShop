package com.shop.servlet.dto;

import com.shop.model.Position;
import com.shop.model.UserTransaction;
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
    private Object quantity;
    private String quantityType;
}
