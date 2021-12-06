package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BitternessValidator implements Validator<AddPositionRequest> {
    private final Integer minBitterness;
    private final Integer maxBitterness;
    private final String message;

    @Override
    public boolean isValid(AddPositionRequest value) {
        return value.getBitterness() <= minBitterness | value.getBitterness() >= maxBitterness;
    }

    @Override
    public String getResult() {
        return message;
    }
}
