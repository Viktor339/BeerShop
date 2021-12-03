package com.shop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class NotNullFieldValidator<T> implements Validator<T> {

    private final Function<T, ?> getter;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return getter.apply(value) == null || !(getter.apply(value).toString().length()>0);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
