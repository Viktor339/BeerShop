package com.shop.service;

import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorServiceTest {

    private List<Validator<AddPositionRequest>> positionRequestValidator;
    private AddPositionRequest addPositionRequest;
    private ValidatorService validatorService;

    @BeforeEach
    public void setUp() {
        validatorService = new ValidatorService();
        positionRequestValidator = Collections.singletonList(
                new NotEmptyFieldValidator<>(AddPositionRequest::getName, "Name is null or empty")
        );
        addPositionRequest = new AddPositionRequest();

    }

    @Test
    void testValidateShouldThrowValidatorException() {

        assertThrows(ValidatorException.class, () ->
                validatorService.validate(positionRequestValidator, addPositionRequest));
    }
}

