package com.shop.service;

import com.shop.service.exception.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageServiceTest {

    private PageService pageService;

    @BeforeEach
    public void setUp() {
        pageService = new PageService();
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void testGetSize(int expectedSize, int userPageSize) {
        assertEquals(expectedSize, pageService.getSize(userPageSize, 1, 3));
    }

    @Test
    void testValidatePageShouldThrowValidatorException() {
        assertThrows(ValidatorException.class, () ->
                pageService.validatePage(0));
    }

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of(2, 2),
                Arguments.of(1, 1),
                Arguments.of(3, 3)
        );
    }
}

