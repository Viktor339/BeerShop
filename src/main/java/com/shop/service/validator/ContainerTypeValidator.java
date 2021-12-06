package com.shop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class ContainerTypeValidator<T> implements Validator<T> {
    private final Function<T, ?> function;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return !(function.apply(value).equals("draft") | function.apply(value).equals("bottle"));
    }

    @Override
    public String getResult() {
        return message;
    }
}
