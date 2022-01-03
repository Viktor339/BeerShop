package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class NameValidatorTest {

    private NameValidator nameValidator;
    private RegistrationRequest registrationRequest;

    @BeforeEach
    public void setUp() {
        nameValidator = new NameValidator();
        registrationRequest = new RegistrationRequest();
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testIsValid(String name, boolean isValid) {
        registrationRequest.setName(name);
        assertEquals(nameValidator.isValid(registrationRequest), isValid);
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                arguments(".", true),
                arguments("U", false)
        );
    }
}

