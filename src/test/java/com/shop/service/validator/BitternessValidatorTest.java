package com.shop.service.validator;

import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitternessValidatorTest {
    private AddPositionRequest addPositionRequest;
    private String message;


    @BeforeEach
    public void setUp() {
        message = "message";
        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setBitterness(1);
    }


    @ParameterizedTest
    @CsvSource(value = {
            "1, 3",
            "1, 0"
    })
    void testIsValidShouldReturnTrue(int min, int max) {
        BitternessValidator bitternessValidator = new BitternessValidator(min, max, "Not all who wander are lost");

        assertTrue(bitternessValidator.isValid(addPositionRequest));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0, 3"
    })
    void testIsValidShouldReturnFalse(int min, int max) {
        BitternessValidator bitternessValidator = new BitternessValidator(min, max, "Not all who wander are lost");
        assertFalse(bitternessValidator.isValid(addPositionRequest));
    }


    @Test
    void testGetMessage() {
        BitternessValidator bitternessValidator = new BitternessValidator(0, 1, message);
        assertEquals(message, bitternessValidator.getMessage());
    }
}

