package com.shop.servlet.dto;

import lombok.Data;

@Data
public class ChangePositionResponse {
    private long id;
    private String name;
    private String containerType;
    private String containerVolume;
    private String quantity;

    public ChangePositionResponse(long id, String name, String containerType, String containerVolume, String quantity) {
        this.id = id;
        this.name = name;
        this.containerType = containerType;
        this.containerVolume = containerVolume;
        this.quantity = quantity;
    }
}
