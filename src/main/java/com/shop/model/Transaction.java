package com.shop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Transaction {
    private Object uuid;
    private String name;
    private Integer quantity;
}
