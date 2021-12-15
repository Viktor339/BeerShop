package com.shop.servlet.request;

import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import lombok.Data;

import java.util.List;

@Data
public class BuyPositionRequest {
    private List<BuyBottleBeerData> bottle;
    private List<BuyDraftBeerData> draft;
}
