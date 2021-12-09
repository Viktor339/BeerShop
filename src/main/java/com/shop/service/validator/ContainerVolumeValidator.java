package com.shop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class ContainerVolumeValidator<T> implements Validator<T> {
    private final Function<T, Double> function;
    private final Double minVolume;
    private final Double maxVolume;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return !(function.apply(value) > minVolume & function.apply(value) < maxVolume);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
