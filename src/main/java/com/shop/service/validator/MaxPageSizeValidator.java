package com.shop.service.validator;

import com.shop.servlet.request.GetUserHistoryRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MaxPageSizeValidator implements Validator<GetUserHistoryRequest> {
    private final Integer maxSize;

    @Override
    public boolean isValid(GetUserHistoryRequest value) {
        return (value.getQuantity() > maxSize);
    }

    @Override
    public Object getResult() {
        return maxSize;
    }
}