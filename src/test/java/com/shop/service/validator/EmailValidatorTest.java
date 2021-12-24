package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

    private EmailValidator emailValidator;
    private RegistrationRequest registrationRequest;
    private String message;


    @BeforeEach
    public void setUp() {
        emailValidator = new EmailValidator();
        registrationRequest = new RegistrationRequest();
        message = "Incorrect email";
    }

    @Test
    void testIsValidShouldReturnFalse() {
        registrationRequest.setEmail("jane.doe@example.org");
        assertFalse(emailValidator.isValid(registrationRequest));
    }

    @Test
    void testIsValidShouldReturnTrue() {
        registrationRequest.setEmail(".....");
        assertTrue(emailValidator.isValid(registrationRequest));
    }

    @Test
    void testGetMessage() {
        assertEquals(message, emailValidator.getMessage());
    }
}

