package com.shop.service.validator;

import com.shop.servlet.request.GetUserHistoryRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MinPageSizeValidator implements Validator<GetUserHistoryRequest> {
    private final Integer minSize;

    @Override
    public boolean isValid(GetUserHistoryRequest value) {
        return (value.getQuantity() < minSize);
    }

    @Override
    public Object getResult() {
        return minSize;
    }
}
