package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EmailValidatorTest {

    private EmailValidator emailValidator;
    private RegistrationRequest registrationRequest;

    @BeforeEach
    public void setUp() {
        emailValidator = new EmailValidator();
        registrationRequest = new RegistrationRequest();
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(String email, boolean isValid) {
        registrationRequest.setEmail(email);
        assertEquals(emailValidator.isValid(registrationRequest), isValid);
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments("jane.doe@example.org", false),
                arguments(".....", true)
        );
    }
}

