package com.shop.service;

import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.Validator;

import java.util.List;
import java.util.Optional;

public class ValidatorService {

    public <T> void validate(List<Validator<T>> validator, T data) {

        final Optional<Object> invalidDraftBeerValidatorMessage = validator.stream()
                .filter(v -> v.isValid(data))
                .findFirst()
                .map(Validator::getResult);

        if (invalidDraftBeerValidatorMessage.isPresent()) {
            throw new ValidatorException(invalidDraftBeerValidatorMessage.get().toString());
        }
    }
}
