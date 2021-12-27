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

class ContainerTypeValidatorTest {

    private static final String MESSAGE = "message";
    private final Function<Object, Object> function = (Function<Object, Object>) mock(Function.class);
    private ContainerTypeValidator<Object> containerTypeValidator;

    @BeforeEach
    public void setUp() {
        containerTypeValidator = new ContainerTypeValidator<>(function, MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(String type, boolean isValid) {
        when(function.apply(any())).thenReturn(type);
        assertEquals((containerTypeValidator).isValid(any()), isValid);
        verify(function, atLeast(1)).apply(any());
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments("value", true),
                arguments("draft", false)
        );
    }
}

