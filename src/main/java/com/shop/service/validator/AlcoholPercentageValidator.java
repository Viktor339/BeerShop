package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlcoholPercentageValidator implements Validator<AddPositionRequest> {
    private final String minPercentage;
    private final String maxPercentage;
    private final String message;

    @Override
    public boolean isValid(AddPositionRequest value) {
        return value.getAlcoholPercentage() <= Double.parseDouble(minPercentage) | value.getAlcoholPercentage() >= Integer.parseInt(maxPercentage);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
