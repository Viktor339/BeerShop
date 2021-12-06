package com.shop.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BuyPositionTransactionDTO {
    private Object uuid;
    private String name;
    private Integer quantity;
}
