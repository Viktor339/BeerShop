package com.shop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class NotNullFieldValidator<T> implements Validator<T> {
    private final Predicate<T> predicateFunction;
    private final Function<T,String> message;

    @Override
    public boolean isValid(T value) {
        return !predicateFunction.test(value);
    }

    @Override
    public String getMessage(T value) {
        return message.apply(value);
    }
}
