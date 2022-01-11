package com.shop.servlet.request;

import lombok.Data;

@Data
public class ChangePositionRequest {
    private Integer id;
    private Double containerVolume;
    private Integer quantity;
}
