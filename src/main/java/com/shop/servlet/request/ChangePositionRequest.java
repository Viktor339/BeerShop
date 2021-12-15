package com.shop.servlet.request;

import lombok.Data;

@Data
public class ChangePositionRequest {
    private Long id;
    private Double containerVolume;
    private Integer quantity;
}
