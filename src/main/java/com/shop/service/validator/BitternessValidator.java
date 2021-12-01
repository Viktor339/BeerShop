package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BitternessValidator implements Validator<AddPositionRequest> {
    private final String minBitterness;
    private final String maxBitterness;
    private final String message;

    @Override
    public boolean isValid(AddPositionRequest value) {
        return Integer.parseInt(value.getBitterness()) <= Integer.parseInt(minBitterness) |
                Integer.parseInt(value.getBitterness()) >= Integer.parseInt(maxBitterness);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
