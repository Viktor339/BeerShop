package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AlcoholPercentageValidatorTest {

    private static final String MESSAGE = "message";
    private AlcoholPercentageValidator alcoholPercentageValidator;

    @BeforeEach
    public void setUp() {
        alcoholPercentageValidator = new AlcoholPercentageValidator(5.0, 20.0,
                MESSAGE);

    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(double alcoholPercentage, boolean isValid) {

        AddPositionRequest addPositionRequest = new AddPositionRequest();
        addPositionRequest.setAlcoholPercentage(alcoholPercentage);

        assertEquals(alcoholPercentageValidator.isValid(addPositionRequest), isValid);
    }


    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments(4.0, true),
                arguments(21.0, true),
                arguments(7.0, false)
        );
    }
}

