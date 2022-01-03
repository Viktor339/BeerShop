package com.shop.service;

import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ValidatorServiceTest {

    public static final String MESSAGE = "message";
    private List<Validator<AddPositionRequest>> positionRequestValidator;
    private AddPositionRequest addPositionRequest;
    private ValidatorService validatorService;

    @BeforeEach
    public void setUp() {
        validatorService = new ValidatorService();
        positionRequestValidator = List.of(
                (Validator<AddPositionRequest>) mock(Validator.class)
        );
        addPositionRequest = new AddPositionRequest();

    }

    @Test
    void testValidateShouldThrowValidatorException() {
        Mockito.when(positionRequestValidator
                .get(0)
                .isValid(addPositionRequest)
        ).thenReturn(true);

        Mockito.when(positionRequestValidator
                .get(0).getMessage()).thenReturn(MESSAGE);

        assertThrows(ValidatorException.class, () ->
                validatorService.validate(positionRequestValidator, addPositionRequest));
    }

    @Test
    void testValidate() {
        validatorService.validate(positionRequestValidator, addPositionRequest);
        verify(positionRequestValidator.get(0), times(1)).isValid(addPositionRequest);
        verify(positionRequestValidator.get(0), times(0)).getMessage();
    }
}

