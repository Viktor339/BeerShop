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

class NotEmptyFieldValidatorTest {

    private final Function<Object, Object> function = (Function<Object, Object>) mock(Function.class);
    private NotEmptyFieldValidator<Object> notEmptyFieldValidator;
    private final String message = "message";

    @BeforeEach
    public void setUp() {
        notEmptyFieldValidator = new NotEmptyFieldValidator<>(function, message);
    }

    @Test
    void testIsValidShouldReturnFalse() {
        when(function.apply(any())).thenReturn("Apply");
        assertFalse(notEmptyFieldValidator.isValid("Value"));
        verify(function, atLeast(1)).apply(any());
    }

    @Test
    void testIsValidWhenFieldIsNullShouldReturnTrue() {
        when(function.apply(any())).thenReturn(null);
        assertTrue(notEmptyFieldValidator.isValid("Value"));
        verify(function).apply(any());
    }

    @Test
    void testIsValidWhenFieldIsEmptyShouldReturnTrue() {
        when(function.apply(any())).thenReturn("");
        assertTrue(notEmptyFieldValidator.isValid("Value"));
        verify(function, atLeast(1)).apply(any());
    }

    @Test
    void testGetMessage() {
        assertEquals(message, notEmptyFieldValidator.getMessage());
    }
}

