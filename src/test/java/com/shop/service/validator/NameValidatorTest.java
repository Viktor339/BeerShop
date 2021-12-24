package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameValidatorTest {
    private NameValidator nameValidator;
    private RegistrationRequest registrationRequest;
    private String message;

    @BeforeEach
    public void setUp() {
        nameValidator = new NameValidator();
        registrationRequest = new RegistrationRequest();
        message = "Incorrect name. Name must contain only Latin characters";
    }

    @Test
    void testIsValidShouldReturnTrue() {
        registrationRequest.setName(".");
        assertTrue(nameValidator.isValid(registrationRequest));
    }

    @Test
    void testIsValidShouldReturnFalse() {
        registrationRequest.setName("U");
        assertFalse(nameValidator.isValid(registrationRequest));
    }

    @Test
    void testGetMessage() {
        assertEquals(message, nameValidator.getMessage());
    }
}

