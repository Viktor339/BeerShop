package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AlcoholPercentageValidatorTest {

    private AlcoholPercentageValidator alcoholPercentageValidator;
    private String message;

    @BeforeEach
    public void setUp() {
        alcoholPercentageValidator = new AlcoholPercentageValidator(5.0, 20.0,
                "message");
        message = "message";
    }

    @ParameterizedTest
    @MethodSource("validAlcoholPercentage")
    void testIsValidShouldReturnFalse(double alcoholPercentage) {

        AddPositionRequest addPositionRequest = new AddPositionRequest();
        addPositionRequest.setAlcoholPercentage(alcoholPercentage);

        assertFalse(
                alcoholPercentageValidator.isValid(addPositionRequest)
        );
    }

    @ParameterizedTest
    @MethodSource("inValidAlcoholPercentage")
    void testIsValidShouldReturnTrue(double alcoholPercentage) {

        AddPositionRequest addPositionRequest = new AddPositionRequest();
        addPositionRequest.setAlcoholPercentage(alcoholPercentage);

        assertTrue(
                alcoholPercentageValidator.isValid(addPositionRequest)
        );
    }


    @Test
    void testGetMessage() {
        assertEquals(message, alcoholPercentageValidator.getMessage());
    }


    static Stream<Arguments> validAlcoholPercentage() {
        return Stream.of(
                arguments(7.0)
        );
    }

    static Stream<Arguments> inValidAlcoholPercentage() {
        return Stream.of(
                arguments(4.0),
                arguments(21.0)
        );
    }
}

