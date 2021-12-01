package com.shop.servlet.request;

import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.model.DraftBeerData;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@Data
public class AddPositionRequest {

    private String name;
    private String beerType;
    private int alcoholPercentage;
    private String bitterness;
    private String containerType;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "containerType", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
    @JsonSubTypes(value = {
            @JsonSubTypes.Type(value = BottleBeerData.class, name = "bottle"),
            @JsonSubTypes.Type(value = DraftBeerData.class, name = "draft")
    })
    private BeerInfo beerInfo;
}
