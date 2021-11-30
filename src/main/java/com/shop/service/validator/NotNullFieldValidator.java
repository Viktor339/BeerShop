package com.shop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class NotNullFieldValidator<T> implements Validator<T> {
    //    private final Predicate<T> predicateFunction;
//    private final Function<T,String> message;
    private final Function<T, ?> getter;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return getter.apply(value) == null;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
