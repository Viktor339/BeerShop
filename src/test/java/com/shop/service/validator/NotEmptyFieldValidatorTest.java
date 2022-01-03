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

class NotEmptyFieldValidatorTest {

    private static final String MESSAGE = "message";
    private final Function<Object, Object> function = (Function<Object, Object>) mock(Function.class);
    private NotEmptyFieldValidator<Object> notEmptyFieldValidator;

    @BeforeEach
    public void setUp() {
        notEmptyFieldValidator = new NotEmptyFieldValidator<>(function, MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(String result, boolean isValid) {
        when(function.apply(any())).thenReturn(result);
        assertEquals(notEmptyFieldValidator.isValid("Value"), isValid);
        verify(function, atLeast(1)).apply(any());
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments("Apply", false),
                arguments(null, true),
                arguments("", true)
        );
    }
}

