package com.shop.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class UserTransaction {
    private Integer positionId;
    private BuyBeerQuantity quantity;
    private Integer userId;
    private Instant created;
}
