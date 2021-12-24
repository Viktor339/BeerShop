package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlcoholPercentageValidator implements Validator<AddPositionRequest> {
    private final Double minPercentage;
    private final Double maxPercentage;
    private final String message;

    @Override
    public boolean isValid(AddPositionRequest value) {
        return (value.getAlcoholPercentage() <= minPercentage) | (value.getAlcoholPercentage() >= maxPercentage);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
