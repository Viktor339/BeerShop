package com.shop.servlet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class GetUserHistoryTransactionDTO {
    private String name;
    private Integer quantity;
    private String date;
}
