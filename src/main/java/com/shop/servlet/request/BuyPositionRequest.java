package com.shop.servlet.request;

import com.shop.model.BuyBeerData;
import lombok.Data;

import java.util.List;

@Data
public class BuyPositionRequest {
    private List<BuyBeerData> bottle;
    private List<BuyBeerData> draft;
}
