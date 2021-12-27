package com.shop.service.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContainerVolumeValidatorTest {

    private static final String MESSAGE = "message";
    private final Function<Double, Double> function = (Function<Double, Double>) mock(Function.class);
    private ContainerVolumeValidator<Double> containerVolumeValidator;

    @BeforeEach
    public void setUp() {
        containerVolumeValidator = new ContainerVolumeValidator<>(function, 0.1, 3.0, MESSAGE);

    }


    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(double containerVolume, boolean isValid) {

        when(function.apply(any())).thenReturn(containerVolume);

        assertEquals((containerVolumeValidator).isValid(any()), isValid);
        verify(function, atLeast(1)).apply(any());
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments(0.0, true),
                arguments(4.0, true),
                arguments(2.0, false)
        );
    }
}

