package com.shop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class ContainerVolumeValidator<T> implements Validator<T> {
    private final Predicate<T> predicate;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return !predicate.test(value);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
