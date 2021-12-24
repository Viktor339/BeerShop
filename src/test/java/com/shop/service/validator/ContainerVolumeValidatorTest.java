package com.shop.service.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContainerVolumeValidatorTest {

    private final Function<Double, Double> function = (Function<Double, Double>) mock(Function.class);
    private ContainerVolumeValidator<Double> containerVolumeValidator;
    private String message;

    @BeforeEach
    public void setUp() {
        containerVolumeValidator = new ContainerVolumeValidator<>(function, 0.1, 3.0, "message");

        message = "message";
    }


    @Test
    void testIsValidWhenTakesValidParameterShouldReturnFalse() {

        when(function.apply(any())).thenReturn(2.0);

        assertFalse((containerVolumeValidator).isValid(any()));
        verify(function, atLeast(1)).apply(any());
    }

    @ParameterizedTest
    @MethodSource("invalidContainerVolume")
    void testIsValidWhenTakesInvalidParameterShouldReturnTrue(double containerVolume) {

        when(function.apply(any())).thenReturn(containerVolume);

        assertTrue((containerVolumeValidator).isValid(any()));
        verify(function, atLeast(1)).apply(any());
    }


    @Test
    void testGetMessage() {
        assertEquals(message, containerVolumeValidator.getMessage());
    }

    static Stream<Arguments> invalidContainerVolume() {
        return Stream.of(
                arguments(0.0),
                arguments(4.0)
        );
    }
}

