package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BitternessValidatorTest {

    private static final String MESSAGE = "message";
    private AddPositionRequest addPositionRequest;

    @BeforeEach
    public void setUp() {
        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setBitterness(1);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(int min, int max, boolean isValid) {
        BitternessValidator bitternessValidator = new BitternessValidator(min, max, MESSAGE);

        assertEquals(bitternessValidator.isValid(addPositionRequest), isValid);
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments(1, 3, true),
                arguments(1, 0, true),
                arguments(0, 3, false)
        );
    }
}

