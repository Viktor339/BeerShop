package com.shop.servlet.dto;

import com.shop.model.BuyBeerQuantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class GetUserHistoryDto {
    private Integer positionId;
    private BuyBeerQuantity quantity;
    private Object date;
}
