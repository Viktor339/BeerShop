package com.shop.service.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContainerTypeValidatorTest {

    private final Function<Object, Object> function = (Function<Object, Object>) mock(Function.class);
    private String message;
    private ContainerTypeValidator<Object> containerTypeValidator;

    @BeforeEach
    public void setUp() {
        message = "message";
        containerTypeValidator = new ContainerTypeValidator<>(function, message);
    }

    @Test
    void testIsValidShouldReturnTrue() {
        when(function.apply(any())).thenReturn("value");
        assertTrue((new ContainerTypeValidator<>(function, message)).isValid(any()));
        verify(function, atLeast(1)).apply(any());
    }

    @Test
    void testIsValidShouldReturnFalse() {
        when(function.apply(any())).thenReturn("draft");
        assertFalse((new ContainerTypeValidator<>(function, message)).isValid(any()));
        verify(function, atLeast(1)).apply(any());
    }

    @Test
    void testGetMessage() {
        assertEquals(message, containerTypeValidator.getMessage());
    }
}

