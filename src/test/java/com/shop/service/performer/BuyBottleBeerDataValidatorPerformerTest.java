package com.shop.service.performer;

import com.shop.model.BuyBottleBeerData;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.BuyPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

class BuyBottleBeerDataValidatorPerformerTest {

    private static final String RESULT = "Id is null or empty";

    private BuyBottleBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer;
    private List<Validator<BuyBottleBeerData>> buyBottleBeerDataValidator;
    private BuyPositionRequest buyPositionRequest;

    @BeforeEach
    public void setUp() {

        buyPositionRequest = new BuyPositionRequest();
        buyPositionRequest.setBottle(Collections.singletonList(new BuyBottleBeerData(null, 1)));

        buyBottleBeerDataValidator = Collections.singletonList(
                (Validator<BuyBottleBeerData>) Mockito.mock(Validator.class)
        );

        buyBottleBeerDataValidatorPerformer = new BuyBottleBeerDataValidatorPerformer(buyBottleBeerDataValidator);
    }


    @Test
    void testIsValid() {

        assertTrue(buyBottleBeerDataValidatorPerformer.isValid(buyPositionRequest));
    }

    @Test
    void testPerformShouldReturnMessage() {

        when(buyBottleBeerDataValidator.get(0).isValid(buyPositionRequest.getBottle().get(0))).thenReturn(true);
        when(buyBottleBeerDataValidator.get(0).getMessage()).thenReturn(RESULT);
        assertEquals(RESULT, buyBottleBeerDataValidatorPerformer.perform(buyPositionRequest));
    }


    @Test
    void testPerformShouldReturnNull() {

        BuyBottleBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer = new BuyBottleBeerDataValidatorPerformer(
                new ArrayList<>());
        buyPositionRequest.setBottle(new ArrayList<>());

        assertNull(buyBottleBeerDataValidatorPerformer.perform(buyPositionRequest));
    }
}

