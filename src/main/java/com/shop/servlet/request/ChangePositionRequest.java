package com.shop.servlet.request;

import lombok.Data;

@Data
public class ChangePositionRequest {
    private String name;
    private String containerType;
    private String newContainerVolume;
    private String newQuantity;
}
