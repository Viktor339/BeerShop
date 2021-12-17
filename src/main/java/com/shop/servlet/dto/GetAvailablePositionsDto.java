package com.shop.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class GetAvailablePositionsDto {
    private Integer positionId;
}
