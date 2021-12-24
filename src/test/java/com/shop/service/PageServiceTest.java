package com.shop.service;

import com.shop.service.exception.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageServiceTest {

    private PageService pageService;

    @BeforeEach
    public void setUp() {
        pageService = new PageService();
    }

    @Test
    void testGetSize() {
        assertEquals(2, pageService.getSize(2, 1, 3));
        assertEquals(1, pageService.getSize(1, 1, 3));
        assertEquals(3, pageService.getSize(3, 1, 3));
    }

    @Test
    void testValidatePageShouldThrowValidatorException() {
        assertThrows(ValidatorException.class, () ->
                pageService.validatePage(0));
    }
}

